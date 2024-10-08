package net.webius.playlist.login.kakao;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Controller
public class LoginKakaoController {
    private final LoginServiceImpl loginService;
    private final LoginKakaoServiceImpl loginKakaoService;
    private final HostConnectionUtil hostConnectionUtil;

    public LoginKakaoController(LoginServiceImpl loginService, LoginKakaoServiceImpl loginKakaoService, HostConnectionUtil hostConnectionUtil) {
        this.loginService = loginService;
        this.loginKakaoService = loginKakaoService;
        this.hostConnectionUtil = hostConnectionUtil;
    }

    @GetMapping("/login/kakao.init")
    public ModelAndView loginKakaoInit(@RequestParam(value = "url", defaultValue = "/") String url,
                                       HttpServletRequest request,
                                       ModelAndView modelAndView) {

        // API 세션 설정
        request.getSession().setAttribute("apiName", "kakao");

        // 리턴 URL 저장
        request.getSession().setAttribute("KakaoReturnUrl", url);

        // 이미 토큰 세션이 있는 경우
        if (request.getSession().getAttribute("kakaoToken") != null) {
            modelAndView.setViewName("/login/loginApi");
            return modelAndView;
        }

        String STATE = StringUtil.generateEncodedString(30);
        String FQDN = (request.getHeader("X-Forwarded-Proto") != null ? request.getHeader("X-Forwarded-Proto") : request.getScheme()) + "://" + request.getServerName();
        if ("http".equals((request.getHeader("X-Forwarded-Proto") != null ? request.getHeader("X-Forwarded-Proto") : request.getScheme())) && 80 != request.getServerPort()) {
            FQDN += ":" + request.getServerPort();
        }
        request.getSession().setAttribute("KAKAO_STATE", STATE);
        modelAndView.setView(MVCUtil.redirect(loginKakaoService.init(STATE, FQDN)));
        return modelAndView;
    }

    @GetMapping("/login/kakao.callback")
    public String loginKakaoCallback(@RequestParam(value = "code", required = false) String code,
                                     @RequestParam(value = "state", required = false) String state,
                                     @RequestParam(value = "error", required = false) String error,
                                     HttpServletRequest request,
                                     ModelMap modelMap) {

        if (error != null) {
            // 오류가 발생한 경우
            modelMap.addAttribute("error", new LoginApiErrorVO("KakaoAPIDenied", "요청이 거부되었습니다."));
        } else if (code == null) {
            // 비 정상 접근
            modelMap.addAttribute("error", new LoginApiErrorVO("KakaoAPINotAllowed", "비정상 접근이 감지되었습니다."));
        } else if (state == null) {
            // 비 정상 접근
            modelMap.addAttribute("error", new LoginApiErrorVO("KakaoAPIStateError", "식별 값을 확인할 수 없습니다."));
        } else if (!request.getSession().getAttribute("KAKAO_STATE").equals(state)) {
            // STATE가 일치하지 않는 경우
            modelMap.addAttribute("error", new LoginApiErrorVO("KakaoAPIStateMismatch", "식별 값이 일치하지 않습니다."));
        }

        // 오류가 발생한 경우
        if (modelMap.getAttribute("error") != null)
            return "/login/loginApiError";

        String FQDN = (request.getHeader("X-Forwarded-Proto") != null ? request.getHeader("X-Forwarded-Proto") : request.getScheme()) + "://" + request.getServerName();
        if ("http".equals((request.getHeader("X-Forwarded-Proto") != null ? request.getHeader("X-Forwarded-Proto") : request.getScheme())) && 80 != request.getServerPort()) {
            FQDN += ":" + request.getServerPort();
        }

        // 정상 접근의 경우
        LoginKakaoCodeVO kakaoCode = loginKakaoService.request(code, FQDN);
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("grant_type", kakaoCode.getGrant_type());
        parameters.put("client_id", kakaoCode.getClient_id());
        parameters.put("client_secret", kakaoCode.getClient_secret());
        parameters.put("redirect_uri", kakaoCode.getRedirect_uri());
        parameters.put("code", kakaoCode.getCode());

        try {
            JsonNode node = hostConnectionUtil.getNode(hostConnectionUtil.post(kakaoCode.getRequest_uri(), parameters).body());
            LoginKakaoTokenVO kakaoToken = new LoginKakaoTokenVO(
                    node.get("access_token").getTextValue(),
                    node.get("token_type").getTextValue(),
                    node.get("refresh_token").getTextValue(),
                    node.get("expires_in").getTextValue(),
                    node.get("refresh_token_expires_in").getTextValue());
            request.getSession().setAttribute("kakaoToken", kakaoToken);
        } catch (HostConnectionException e) {
            e.printStackTrace();
        }
        return "/login/loginApi";
    }

    @GetMapping("/login/kakao.login")
    public ModelAndView loginKakaoLogin(HttpServletRequest request,
                                        ModelAndView modelAndView) {

        LoginKakaoTokenVO kakaoToken = (LoginKakaoTokenVO) request.getSession().getAttribute("kakaoToken");

        // 토큰이 없는 경우
        if (kakaoToken == null) {
            modelAndView.addObject("error", new LoginApiErrorVO("KakaoAPITokenError", "토큰을 확인할 수 없습니다."));
            modelAndView.setViewName("/login/loginApiError");
            return modelAndView;
        }

        // 토큰 만료 조회 및 재설정
        try {
            // 토큰 만료 검증용 정보 수집
            HashMap<String, String> params = new HashMap<String, String>();
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Authorization", kakaoToken.getToken_type() + " " + kakaoToken.getAccess_token());
            Connection.Response response = hostConnectionUtil.get(loginKakaoService.verify(), params, headers);

            // 토큰 정보가 손상된 경우
            if (response.statusCode() == 400) {
                modelAndView.addObject("error", new LoginApiErrorVO("KakaoAPITokenInfoNotFound", "토큰 정보가 잘못되었습니다."));
                modelAndView.setViewName("/login/loginApiError");
                return modelAndView;
            }

            // 토큰이 만료된 경우
            if (response.statusCode() == 401) {
                // 토큰 재설정 정보 수집
                LoginKakaoRefreshTokenVO kakaoRefreshToken = loginKakaoService.refresh(kakaoToken.getRefresh_token());
                params.put("grant_type", kakaoRefreshToken.getGrant_type());
                params.put("client_id", kakaoRefreshToken.getClient_id());
                params.put("client_secret", kakaoRefreshToken.getClient_secret());
                params.put("refresh_token", kakaoRefreshToken.getRefresh_token());

                // 토큰 재설정 요청 후 결과 저장
                JsonNode node = hostConnectionUtil.getNode(hostConnectionUtil.post(kakaoRefreshToken.getRequest_uri(), params).body());
                kakaoToken = new LoginKakaoTokenVO(
                        node.get("access_token").getTextValue(),
                        node.get("token_type").getTextValue(),
                        node.get("refresh_token").getTextValue(),
                        node.get("expires_in").getTextValue(),
                        node.get("refresh_token_expires_in").getTextValue());
                
                // 세션에 토큰 정보 저장
                request.getSession().setAttribute("kakaoToken", kakaoToken);
            } else if (response.statusCode() != 200) {
                // 이외 비정상 상태 코드가 반환된 경우
                modelAndView.addObject("error", new LoginApiErrorVO("KakaoAPITokenServerError", "토큰 서버가 일시적으로 응답하지 않습니다. 관리자에 문의 바랍니다."));
                modelAndView.setViewName("/login/loginApiError");
                return modelAndView;
            }
        } catch (HostConnectionException e) {
            e.printStackTrace();
        }
        
        // 로그인 정보 조회
        try {
            // 반환 받을 프로필 정보
            ObjectMapper objectMapper = new ObjectMapper();
            List<String> property_keys_list = new ArrayList<String>();
            property_keys_list.add("properties.nickname");
            String property_keys = objectMapper.writeValueAsString(property_keys_list);

            // 정보 조회에 사용될 파라미터와 헤더 설정
            HashMap<String, String> params = new HashMap<String, String>();
            HashMap<String, String> headers = new HashMap<String, String>();
            params.put("property_keys", property_keys);
            headers.put("Authorization", kakaoToken.getToken_type() + " " + kakaoToken.getAccess_token());

            // 정보 조회
            Connection.Response response = hostConnectionUtil.post(loginKakaoService.user(), params, headers);
            JsonNode node = hostConnectionUtil.getNode(response.body());
            LoginApiVO kakao = new LoginApiVO(
                    node.get("id").toString(),
                    node.get("properties").get("nickname").getTextValue());
            request.getSession().setAttribute("apiData", kakao);

            UserVO userVO = loginService.doLogin(kakao, "kakao");
            // 로그인 성공 시
            if (userVO != null) {
                modelAndView.setView(MVCUtil.redirect((String) request.getSession().getAttribute("KakaoReturnUrl")));

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
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        return modelAndView;
    }
}
