package net.webius.playlist.login.naver;

import org.springframework.stereotype.Service;

@Service("loginNaverService")
public class LoginNaverServiceImpl implements LoginNaverService {
    private final String[] URL_TEMPLATE = {
            "https://nid.naver.com/oauth2.0/authorize", // 인증 코드 요청 URL
            "https://nid.naver.com/oauth2.0/token", // 토큰 발급/재발급 요청 URL
            "https://openapi.naver.com/v1/nid/verify", // 토큰 상태 확인 URL
            "https://openapi.naver.com/v1/nid/me" // 사용자 정보 조회 URL
    };
    private final String[] GRANT_TYPE = {
            "authorization_code", "refresh_token"
    };

    public String init(String state, String fqdn) {
        return URL_TEMPLATE[0]
                + "?client_id=" + LoginNaverService.CLIENT_ID
                + "&redirect_uri=" + fqdn + LoginNaverService.REDIRECT_URI
                + "&response_type=code"
                + "&state=" + state;
    }

    public LoginNaverCodeVO request(String code, String fqdn) {
        return new LoginNaverCodeVO(URL_TEMPLATE[1],
                GRANT_TYPE[0],
                LoginNaverService.CLIENT_ID,
                LoginNaverService.CLIENT_SECRET,
                fqdn + LoginNaverService.REDIRECT_URI,
                code);
    }

    public String verify() {
        return URL_TEMPLATE[2];
    }

    public LoginNaverRefreshTokenVO refresh(String refresh_token) {
        return new LoginNaverRefreshTokenVO(URL_TEMPLATE[1],
                GRANT_TYPE[1],
                LoginNaverService.CLIENT_ID,
                LoginNaverService.CLIENT_SECRET,
                refresh_token);
    }

    public String user() {
        return URL_TEMPLATE[3];
    }
}
