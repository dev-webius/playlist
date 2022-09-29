package net.webius.playlist.sign;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignAlertVO {
    private String status;
    private String message;

    public SignAlertVO(String status) {
        this.status = status;
    }
}
