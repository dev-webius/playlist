package net.webius.playlist.util;

import org.springframework.web.servlet.view.RedirectView;

public class MVCUtil {
    public static RedirectView redirect(String url) {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);
        redirectView.setExposeModelAttributes(false);
        return redirectView;
    }
}
