package net.webius.playlist.login.naver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginNaverTokenVO {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private String expires_in;
}
