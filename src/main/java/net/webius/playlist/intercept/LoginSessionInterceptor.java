package net.webius.playlist.intercept;

import lombok.extern.slf4j.Slf4j;
import net.webius.playlist.login.LoginCheckUtil;
import net.webius.playlist.util.UrlUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginSessionInterceptor implements HandlerInterceptor {
    private final LoginCheckUtil loginCheckUtil;

    private final String NO_SESSION_ACCESS_LOGOUT = "잘못된 접근입니다.";
    private final String HAS_SESSION_ACCESS_SIGN = "잘못된 접근입니다.";

    public LoginSessionInterceptor(LoginCheckUtil loginCheckUtil) {
        this.loginCheckUtil = loginCheckUtil;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        switch (loginCheckUtil.check(request.getRequestURI())) {
            // 로그인 체크
            case 0:
                if (!loginCheckUtil.hasLogin(request.getSession())) {
                    if (request.getRequestURI().equals("/logout")) {
                        // 로그아웃 시 로그인 정보가 없는 경우 발생
                        response.setContentType("text/html; charset=UTF-8");
                        response.getWriter().println("<script>alert('" + NO_SESSION_ACCESS_LOGOUT + "'); location.href = '/';</script>");
                    } else {
                        // 이외 로그인 정보가 필요한 모든 경우
                        response.sendRedirect("/login?url=" + UrlUtil.getUrl(request));
                    }
                    return false;
                }
                break;

            // 비 로그인 체크
            case 1:
                // 로그인/회원가입 시 로그인 상태인 경우 발생
                if (loginCheckUtil.hasLogin(request.getSession())) {
                    response.setContentType("text/html; charset=UTF-8");
                    response.getWriter().println("<script>alert('" + HAS_SESSION_ACCESS_SIGN + "'); location.href = '/';</script>");
                    return false;
                }
                break;

            // 예외 URL
            default:
                break;
        }
        return true;
    }
}