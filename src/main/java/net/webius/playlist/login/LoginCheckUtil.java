package net.webius.playlist.login;

import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
public class LoginCheckUtil {
    List<String> NOT_LOGIN;
    List<String> NOT_CHECK;

    private final String USER_SESSION_NAME = "user";

    public int check(String url) {
        Pattern pattern;
        Matcher matcher;

        // 예외 URL 패턴
        for (String regexp : NOT_CHECK) {
            pattern = Pattern.compile(regexp);
            matcher = pattern.matcher(url);
            if (matcher.find())
                return 2;
        }

        // 비 로그인 필요 URL 패턴
        for (String regexp : NOT_LOGIN) {
            pattern = Pattern.compile(regexp);
            matcher = pattern.matcher(url);
            if (matcher.find())
                return 1;
        }

        // 로그인 필요
        return 0;
    }

    public Boolean hasLogin(HttpSession session) {
        return session.getAttribute(USER_SESSION_NAME) != null;
    }

    public Boolean verify(LoginVO loginVO) {
        // Null 검증
        if (loginVO.getId() == null)
            return false;
        if (loginVO.getPw() == null)
            return false;

        return true;
    }
}
