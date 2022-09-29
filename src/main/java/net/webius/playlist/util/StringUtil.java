package net.webius.playlist.util;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

@Slf4j
public class StringUtil {
    public static String urlEncode(String val) {
        try {
            return URLEncoder.encode(val, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String generateEncodedString(int length) {
        String RANGE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        while (stringBuilder.length() < length) {
            stringBuilder.append(RANGE.charAt((int) (random.nextFloat() * RANGE.length())));
        }
        return stringBuilder.toString();
    }

    public static String pack(String val) {
        return val
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\r\n", "<br/>")
                .replaceAll("\"", "&quot;")
                .replaceAll(" ", "&nbsp;");
    }

    public static String unpack(String val) {
        return val
                .replaceAll("&nbsp;", " ")
                .replaceAll("&quot;", "\"")
                .replaceAll("<br/>", "\r\n")
                .replaceAll("&gt;", ">")
                .replaceAll("&lt;", "<");
    }

    public static Boolean isLimit(String val, int limit) {
        // LIMIT를 넘지 않으면 true, LIMIT를 초과하면 false
        return val.getBytes().length <= limit; // 한글 1자 => 3Byte
    }
}
