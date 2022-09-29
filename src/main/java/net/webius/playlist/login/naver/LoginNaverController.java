package net.webius.playlist.login.naver;

import net.webius.playlist.login.*;
import net.webius.playlist.util.HostConnectionException;
import net.webius.playlist.util.HostConnectionUtil;
import net.webius.playlist.util.MVCUtil;
import net.webius.playlist.util.StringUtil;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Connection;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class LoginNaverController {
    private final LoginServiceImpl loginService;
    private final LoginNaverServiceImpl loginNaverService;
    private final HostConnectionUtil hostConnectionUtil;

    public LoginNaverController(LoginServiceImpl loginService, LoginNaverServiceImpl loginNaverService, HostConnectionUtil hostConnectionUtil) {
        this.loginService = loginService;
        this.loginNaverService = loginNaverService;
        this.hostConnectionUtil = hostConnectionUtil;
    }

    @GetMapping("/login/naver.init")
    public ModelAndView loginNaverInit(@RequestParam(value = "url", defaultValue = "/") String url,
                                       HttpServletRequest request,
                                       ModelAndView modelAndView) {

        // API 세션 설정
        request.getSession().setAttribute("apiName", "naver");

        // 리턴 URL 저장
        request.getSession().setAttribute("NaverReturnUrl", url);

        // 이미 토큰 세션이 있는 경우
        if (request.getSession().getAttribute("naverToken") != null) {
            modelAndView.setViewName("/login/loginApi");
            return modelAndView;
        }

        String STATE = StringUtil.generateEncodedString(30);
        String FQDN = (request.getHeader("X-Forwarded-Proto") != null ? request.getHeader("X-Forwarded-Proto") : request.getScheme()) + "://" + request.getServerName();
        if ("http".equals((request.getHeader("X-Forwarded-Proto") != null ? request.getHeader("X-Forwarded-Proto") : request.getScheme())) && 80 != request.getServerPort()) {
            FQDN += ":" + request.getServerPort();
        }
        request.getSession().setAttribute("NAVER_STATE", STATE);
        modelAndView.setView(MVCUtil.redirect(loginNaverService.init(STATE, FQDN)));
        return modelAndView;
    }

    @GetMapping("/login/naver.callback")
    public String loginNaverCallback(@RequestParam(value = "code", required = false) String code,
                                     @RequestParam(value = "state", required = false) String state,
                                     @RequestParam(value = "error", required = false) String error,
                                     HttpServletRequest request,
                                     ModelMap modelMap) {

        if (error != null) {
            // 오류가 발생한 경우
            modelMap.addAttribute("error", new LoginApiErrorVO("NaverAPIDenied", "요청이 거부되었습니다."));
        } else if (code == null) {
            // 비 정상 접근
            modelMap.addAttribute("error", new LoginApiErrorVO("NaverAPINotAllowed", "비정상 접근이 감지되었습니다."));
        } else if (state == null) {
            // 비 정상 접근
            modelMap.addAttribute("error", new LoginApiErrorVO("NaverAPIStateError", "식별 값을 확인할 수 없습니다."));
        } else if (!request.getSession().getAttribute("NAVER_STATE").equals(state)) {
            // STATE가 일치하지 않는 경우
            modelMap.addAttribute("error", new LoginApiErrorVO("NaverAPIStateMismatch", "식별 값이 일치하지 않습니다."));
        }

        // 오류가 발생한 경우
        if (modelMap.getAttribute("error") != null)
            return "/login/loginApiError";

        String FQDN = (request.getHeader("X-Forwarded-Proto") != null ? request.getHeader("X-Forwarded-Proto") : request.getScheme()) + "://" + request.getServerName();
        if ("http".equals((request.getHeader("X-Forwarded-Proto") != null ? request.getHeader("X-Forwarded-Proto") : request.getScheme())) && 80 != request.getServerPort()) {
            FQDN += ":" + request.getServerPort();
        }

        // 정상 접근의 경우
        LoginNaverCodeVO naverCode = loginNaverService.request(code, FQDN);
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("grant_type", naverCode.getGrant_type());
        parameters.put("client_id", naverCode.getClient_id());
        parameters.put("client_secret", naverCode.getClient_secret());
        parameters.put("redirect_uri", naverCode.getRedirect_uri());
        parameters.put("code", naverCode.getCode());

        try {
            JsonNode node = hostConnectionUtil.getNode(hostConnectionUtil.post(naverCode.getRequest_uri(), parameters).body());
            LoginNaverTokenVO naverToken = new LoginNaverTokenVO(
                    node.get("access_token").getTextValue(),
                    node.get("token_type").getTextValue(),
                    node.get("refresh_token").getTextValue(),
                    node.get("expires_in").getTextValue());
            request.getSession().setAttribute("naverToken", naverToken);
        } catch (HostConnectionException e) {
            e.printStackTrace();
        }
        return "/login/loginApi";
    }

    @GetMapping("/login/naver.login")
    public ModelAndView loginNaverLogin(HttpServletRequest request,
                                        ModelAndView modelAndView) {

        LoginNaverTokenVO naverToken = (LoginNaverTokenVO) request.getSession().getAttribute("naverToken");

        // 토큰이 없는 경우
        if (naverToken == null) {
            modelAndView.addObject("error", new LoginApiErrorVO("NaverAPITokenError", "토큰을 확인할 수 없습니다."));
            modelAndView.setViewName("/login/loginApiError");
            return modelAndView;
        }

        // 토큰 만료 조회 및 재설정
        try {
            // 토큰 만료 검증용 정보 수집
            HashMap<String, String> params = new HashMap<String, String>();
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Authorization", naverToken.getToken_type() + " " + naverToken.getAccess_token());
            Connection.Response response = hostConnectionUtil.get(loginNaverService.verify(), params, headers);

            // 토큰 정보가 손상된 경우
            if (response.statusCode() == 400) {
                modelAndView.addObject("error", new LoginApiErrorVO("NaverAPITokenInfoNotFound", "토큰 정보가 잘못되었습니다."));
                modelAndView.setViewName("/login/loginApiError");
                return modelAndView;
            }

            // 토큰이 만료된 경우
            if (response.statusCode() == 401) {
                // 토큰 재설정 정보 수집
                LoginNaverRefreshTokenVO naverRefreshToken = loginNaverService.refresh(naverToken.getRefresh_token());
                params.put("grant_type", naverRefreshToken.getGrant_type());
                params.put("client_id", naverRefreshToken.getClient_id());
                params.put("client_secret", naverRefreshToken.getClient_secret());
                params.put("refresh_token", naverRefreshToken.getRefresh_token());

                // 토큰 재설정 요청 후 결과 저장
                JsonNode node = hostConnectionUtil.getNode(hostConnectionUtil.post(naverRefreshToken.getRequest_uri(), params).body());
                naverToken = new LoginNaverTokenVO(
                        node.get("access_token").getTextValue(),
                        node.get("token_type").getTextValue(),
                        node.get("refresh_token").getTextValue(),
                        node.get("expires_in").getTextValue());
                
                // 세션에 토큰 정보 저장
                request.getSession().setAttribute("naverToken", naverToken);
            } else if (response.statusCode() != 200) {
                // 이외 비정상 상태 코드가 반환된 경우
                modelAndView.addObject("error", new LoginApiErrorVO("NaverAPITokenServerError", "토큰 서버가 일시적으로 응답하지 않습니다. 관리자에 문의 바랍니다."));
                modelAndView.setViewName("/login/loginApiError");
                return modelAndView;
            }
        } catch (HostConnectionException e) {
            e.printStackTrace();
        }
        
        // 로그인 정보 조회
        try {
            // 정보 조회에 사용될 파라미터와 헤더 설정
            HashMap<String, String> params = new HashMap<String, String>();
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Authorization", naverToken.getToken_type() + " " + naverToken.getAccess_token());

            // 정보 조회
            Connection.Response response = hostConnectionUtil.get(loginNaverService.user(), params, headers);
            JsonNode node = hostConnectionUtil.getNode(response.body());
            LoginApiVO naver = new LoginApiVO(
                    node.get("response").get("id").getTextValue(),
                    node.get("response").get("nickname").getTextValue());
            request.getSession().setAttribute("apiData", naver);

            UserVO userVO = loginService.doLogin(naver, "naver");
            // 로그인 성공 시
            if (userVO != null) {
                modelAndView.setView(MVCUtil.redirect((String) request.getSession().getAttribute("NaverReturnUrl")));

                HttpSession session = request.getSession();
                session.invalidate(); // 새로운 세션 발급
                session = request.getSession();
                session.setAttribute("user", userVO);
            }

            // 로그인 실패 시
            else {
                modelAndView.setViewName("/login/loginApiSign");
            }
        } catch (HostConnectionException e) {
            e.printStackTrace();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        return modelAndView;
    }
}
