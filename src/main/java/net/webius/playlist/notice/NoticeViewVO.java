package net.webius.playlist.notice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeViewVO {
    private String nid;
    private NoticePageVO page;

    public NoticeViewVO(String nid) {
        this.nid = nid;
    }

    public NoticeViewVO(NoticePageVO page) {
        this.page = page;
    }
}
