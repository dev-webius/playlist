package net.webius.playlist.util;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlUtil {
    public static String getUrl(HttpServletRequest request) {
        try {
            if (request.getQueryString() == null)
                return URLEncoder.encode(request.getRequestURL().toString(), "UTF-8");
            else
                return URLEncoder.encode(request.getRequestURL().toString() + "?" + request.getQueryString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getUri(HttpServletRequest request) {
        if (request.getQueryString() == null)
            return request.getRequestURL().toString();
        else
            return request.getRequestURL().toString() + "?" + request.getQueryString();
    }

    public static String encode(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
