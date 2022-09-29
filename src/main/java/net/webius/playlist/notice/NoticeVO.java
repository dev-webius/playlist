package net.webius.playlist.notice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeVO {
    private String nid;
    private String subject;
    private String content;
    private String alias;
    private String cdate;
    private String view;

    public void setCdate(String cdate) {
        this.cdate = cdate.split("\\.0")[0];
    }
}
