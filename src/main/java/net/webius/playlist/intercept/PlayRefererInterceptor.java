package net.webius.playlist.intercept;

import lombok.extern.slf4j.Slf4j;
import net.webius.playlist.util.UrlUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class PlayRefererInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getHeader("Referer") == null) {
            response.sendRedirect(UrlUtil.getUri(request) + "/redirect");
            return false;
        }
        return true;
    }
}