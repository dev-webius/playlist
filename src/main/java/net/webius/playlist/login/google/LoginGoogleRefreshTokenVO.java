package net.webius.playlist.login.google;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginGoogleRefreshTokenVO {
    private String request_uri;
    private String grant_type;
    private String client_id;
    private String client_secret;
    private String refresh_token;
}
