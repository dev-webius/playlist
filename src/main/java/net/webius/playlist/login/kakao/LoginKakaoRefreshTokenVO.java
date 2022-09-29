package net.webius.playlist.login.kakao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginKakaoRefreshTokenVO {
    private String request_uri;
    private String grant_type;
    private String client_id;
    private String client_secret;
    private String refresh_token;
}
