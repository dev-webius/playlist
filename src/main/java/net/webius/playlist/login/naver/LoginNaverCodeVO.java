package net.webius.playlist.login.naver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginNaverCodeVO {
    private String request_uri;
    private String grant_type;
    private String client_id;
    private String client_secret;
    private String redirect_uri;
    private String code;
}
