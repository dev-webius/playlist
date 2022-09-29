package net.webius.playlist.login.kakao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginKakaoTokenVO {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private String expires_in;
    private String expires_in_rt;
}
