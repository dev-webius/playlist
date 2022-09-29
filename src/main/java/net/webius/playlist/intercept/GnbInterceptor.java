package net.webius.playlist.intercept;

import lombok.extern.slf4j.Slf4j;
import net.webius.playlist.login.LoginCheckUtil;
import net.webius.playlist.util.UrlUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class GnbInterceptor implements HandlerInterceptor {
    private final LoginCheckUtil loginCheckUtil;

    public GnbInterceptor(LoginCheckUtil loginCheckUtil) {
        this.loginCheckUtil = loginCheckUtil;
    }

    // Headers
    private final String HEADER_DEFAULT = "/WEB-INF/views/static/include/header.jsp";
    private final String HEADER_LOGIN = "/WEB-INF/views/static/include/header-login.jsp";

    // Footers
    private final String FOOTER_DEFAULT = "/WEB-INF/views/static/include/footer.jsp";

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (loginCheckUtil.hasLogin(request.getSession())) {
            modelAndView.addObject("hdr", HEADER_LOGIN);
        } else {
            modelAndView.addObject("hdr", HEADER_DEFAULT);
        }
        modelAndView.addObject("rturl", UrlUtil.getUrl(request));

        modelAndView.addObject("ftr", FOOTER_DEFAULT);
    }
}
