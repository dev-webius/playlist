package net.webius.playlist.login.google;

import net.webius.playlist.login.*;
import net.webius.playlist.util.HostConnectionException;
import net.webius.playlist.util.HostConnectionUtil;
import net.webius.playlist.util.MVCUtil;
import net.webius.playlist.util.StringUtil;
import org.codehaus.jackson.JsonNode;
import org.jsoup.Connection;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
public class LoginGoogleController {
    private final LoginServiceImpl loginService;
    private final LoginGoogleServiceImpl loginGoogleService;
    private final HostConnectionUtil hostConnectionUtil;

    public LoginGoogleController(LoginServiceImpl loginService, LoginGoogleServiceImpl loginGoogleService, HostConnectionUtil hostConnectionUtil) {
        this.loginService = loginService;
        this.loginGoogleService = loginGoogleService;
        this.hostConnectionUtil = hostConnectionUtil;
    }

    @GetMapping("/login/google.init")
    public ModelAndView loginGoogleInit(@RequestParam(value = "url", defaultValue = "/") String url,
                                       HttpServletRequest request,
                                       ModelAndView modelAndView) {

        // API 세션 설정
        request.getSession().setAttribute("apiName", "google");

        // 리턴 URL 저장
        request.getSession().setAttribute("GoogleReturnUrl", url);

        // 이미 토큰 세션이 있는 경우
        if (request.getSession().getAttribute("googleToken") != null) {
            modelAndView.setViewName("/login/loginApi");
            return modelAndView;
        }

        String STATE = StringUtil.generateEncodedString(30);
        String FQDN = (request.getHeader("X-Forwarded-Proto") != null ? request.getHeader("X-Forwarded-Proto") : request.getScheme()) + "://" + request.getServerName();
        if ("http".equals((request.getHeader("X-Forwarded-Proto") != null ? request.getHeader("X-Forwarded-Proto") : request.getScheme())) && 80 != request.getServerPort()) {
            FQDN += ":" + request.getServerPort();
        }
        request.getSession().setAttribute("GOOGLE_STATE", STATE);
        modelAndView.setView(MVCUtil.redirect(loginGoogleService.init(STATE, FQDN)));
        return modelAndView;
    }

    @GetMapping("/login/google.callback")
    public String loginGoogleCallback(@RequestParam(value = "code", required = false) String code,
                                     @RequestParam(value = "state", required = false) String state,
                                     @RequestParam(value = "error", required = false) String error,
                                     HttpServletRequest request,
                                     ModelMap modelMap) {

        if (error != null) {
            // 오류가 발생한 경우
            modelMap.addAttribute("error", new LoginApiErrorVO("GoogleAPIDenied", "요청이 거부되었습니다."));
        } else if (code == null) {
            // 비 정상 접근
            modelMap.addAttribute("error", new LoginApiErrorVO("GoogleAPINotAllowed", "비정상 접근이 감지되었습니다."));
        } else if (state == null) {
            // 비 정상 접근
            modelMap.addAttribute("error", new LoginApiErrorVO("GoogleAPIStateError", "식별 값을 확인할 수 없습니다."));
        } else if (!request.getSession().getAttribute("GOOGLE_STATE").equals(state)) {
            // STATE가 일치하지 않는 경우
            modelMap.addAttribute("error", new LoginApiErrorVO("GoogleAPIStateMismatch", "식별 값이 일치하지 않습니다."));
        }

        // 오류가 발생한 경우
        if (modelMap.getAttribute("error") != null)
            return "/login/loginApiError";

        String FQDN = (request.getHeader("X-Forwarded-Proto") != null ? request.getHeader("X-Forwarded-Proto") : request.getScheme()) + "://" + request.getServerName();
        if ("http".equals((request.getHeader("X-Forwarded-Proto") != null ? request.getHeader("X-Forwarded-Proto") : request.getScheme())) && 80 != request.getServerPort()) {
            FQDN += ":" + request.getServerPort();
        }

        // 정상 접근의 경우
        LoginGoogleCodeVO googleCode = loginGoogleService.request(code, FQDN);
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("grant_type", googleCode.getGrant_type());
        parameters.put("client_id", googleCode.getClient_id());
        parameters.put("client_secret", googleCode.getClient_secret());
        parameters.put("redirect_uri", googleCode.getRedirect_uri());
        parameters.put("code", googleCode.getCode());

        try {
            JsonNode node = hostConnectionUtil.getNode(hostConnectionUtil.post(googleCode.getRequest_uri(), parameters).body());
            LoginGoogleTokenVO googleToken = new LoginGoogleTokenVO(
                    node.get("access_token").getTextValue(),
                    node.get("id_token").getTextValue(),
                    node.get("token_type").getTextValue(),
                    node.get("scope").getTextValue(),
                    node.get("expires_in").getTextValue());
            request.getSession().setAttribute("googleToken", googleToken);
        } catch (HostConnectionException e) {
            e.printStackTrace();
        }
        return "/login/loginApi";
    }

    @GetMapping("/login/google.login")
    public ModelAndView loginGoogleLogin(HttpServletRequest request,
                                        ModelAndView modelAndView) {

        LoginGoogleTokenVO googleToken = (LoginGoogleTokenVO) request.getSession().getAttribute("googleToken");

        // 토큰이 없는 경우
        if (googleToken == null) {
            modelAndView.addObject("error", new LoginApiErrorVO("GoogleAPITokenError", "토큰을 확인할 수 없습니다."));
            modelAndView.setViewName("/login/loginApiError");
            return modelAndView;
        }

        // Refresh 토큰 없음. 토큰 검증 없이 바로 정보 수집 실행
        // 로그인 정보 조회
        try {
            // 정보 조회에 사용될 파라미터와 헤더 설정
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("id_token", googleToken.getId_token());
            HashMap<String, String> headers = new HashMap<String, String>();

            // 정보 조회
            Connection.Response response = hostConnectionUtil.post(loginGoogleService.user(), params, headers);
            JsonNode node = hostConnectionUtil.getNode(response.body());
            LoginApiVO google = new LoginApiVO(
                    node.get("sub").getTextValue(),
                    node.get("name").getTextValue());
            request.getSession().setAttribute("apiData", google);

            UserVO userVO = loginService.doLogin(google, "google");
            // 로그인 성공 시
            if (userVO != null) {
                modelAndView.setView(MVCUtil.redirect((String) request.getSession().getAttribute("GoogleReturnUrl")));

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
