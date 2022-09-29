package net.webius.playlist.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlayAlertVO {
    private String status;
    private String message;
    private String returnUrl;

    public PlayAlertVO(String status) {
        this.status = status;
    }

    public PlayAlertVO(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
