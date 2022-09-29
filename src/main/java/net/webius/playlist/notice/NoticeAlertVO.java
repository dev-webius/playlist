package net.webius.playlist.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NoticeAlertVO {
    private String status;
    private String message;
    private String returnUrl;

    public NoticeAlertVO(String status) {
        this.status = status;
    }

    public NoticeAlertVO(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
