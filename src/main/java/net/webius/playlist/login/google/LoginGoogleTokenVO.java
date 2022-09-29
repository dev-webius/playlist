package net.webius.playlist.login.google;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginGoogleTokenVO {
    private String access_token;
    private String id_token;
    private String token_type;
    private String scope;
    private String expires_in;
}
