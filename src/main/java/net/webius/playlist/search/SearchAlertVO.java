package net.webius.playlist.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchAlertVO {
    private String status;
    private String message;
    private String returnUrl;

    public SearchAlertVO(String status) {
        this.status = status;
    }

    public SearchAlertVO(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
