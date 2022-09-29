package net.webius.playlist.login.google;

import org.springframework.stereotype.Service;

@Service("loginGoogleService")
public class LoginGoogleServiceImpl implements LoginGoogleService {
    private final String[] URL_TEMPLATE = {
            "https://accounts.google.com/o/oauth2/v2/auth", // 인증 코드 요청 URL
            "https://oauth2.googleapis.com/token", // 토큰 발급/재발급 요청 URL
            "", // 토큰 상태 확인 URL
            "https://oauth2.googleapis.com/tokeninfo" // 사용자 정보 조회 URL
    };
    private final String[] GRANT_TYPE = {
            "authorization_code", "refresh_token"
    };

    public String init(String state, String fqdn) {
        return URL_TEMPLATE[0]
                + "?client_id=" + LoginGoogleService.CLIENT_ID
                + "&redirect_uri=" + fqdn + LoginGoogleService.REDIRECT_URI
                + "&response_type=code"
                + "&state=" + state
                + "&scope=profile";
    }

    public LoginGoogleCodeVO request(String code, String fqdn) {
        return new LoginGoogleCodeVO(URL_TEMPLATE[1],
                GRANT_TYPE[0],
                LoginGoogleService.CLIENT_ID,
                LoginGoogleService.CLIENT_SECRET,
                fqdn + LoginGoogleService.REDIRECT_URI,
                code);
    }

    public String verify() {
        return URL_TEMPLATE[2];
    }

    public LoginGoogleRefreshTokenVO refresh(String refresh_token) {
        return new LoginGoogleRefreshTokenVO(URL_TEMPLATE[1],
                GRANT_TYPE[1],
                LoginGoogleService.CLIENT_ID,
                LoginGoogleService.CLIENT_SECRET,
                refresh_token);
    }

    public String user() {
        return URL_TEMPLATE[3];
    }
}
