package net.webius.playlist.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginApiErrorVO {
    private String code;
    private String message;
}
