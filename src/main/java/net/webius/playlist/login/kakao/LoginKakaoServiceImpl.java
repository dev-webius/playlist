package net.webius.playlist.login.kakao;

import org.springframework.stereotype.Service;

@Service("loginKakaoService")
public class LoginKakaoServiceImpl implements LoginKakaoService {
    private final String[] URL_TEMPLATE = {
            "https://kauth.kakao.com/oauth/authorize", // 인증 코드 요청 URL
            "https://kauth.kakao.com/oauth/token", // 토큰 발급/재발급 요청 URL
            "https://kapi.kakao.com/v1/user/access_token_info", // 토큰 상태 확인 URL
            "https://kapi.kakao.com/v2/user/me" // 사용자 정보 조회 URL
    };
    private final String[] GRANT_TYPE = {
            "authorization_code", "refresh_token"
    };

    public String init(String state, String fqdn) {
        return URL_TEMPLATE[0]
                + "?client_id=" + LoginKakaoService.CLIENT_ID
                + "&redirect_uri=" + fqdn + LoginKakaoService.REDIRECT_URI
                + "&response_type=code"
                + "&state=" + state;
    }

    public LoginKakaoCodeVO request(String code, String fqdn) {
        return new LoginKakaoCodeVO(URL_TEMPLATE[1],
                GRANT_TYPE[0],
                LoginKakaoService.CLIENT_ID,
                LoginKakaoService.CLIENT_SECRET,
                fqdn + LoginKakaoService.REDIRECT_URI,
                code);
    }

    public String verify() {
        return URL_TEMPLATE[2];
    }

    public LoginKakaoRefreshTokenVO refresh(String refresh_token) {
        return new LoginKakaoRefreshTokenVO(URL_TEMPLATE[1],
                GRANT_TYPE[1],
                LoginKakaoService.CLIENT_ID,
                LoginKakaoService.CLIENT_SECRET,
                refresh_token);
    }

    public String user() {
        return URL_TEMPLATE[3];
    }
}
