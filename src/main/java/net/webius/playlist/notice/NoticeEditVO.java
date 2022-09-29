package net.webius.playlist.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NoticeEditVO {
    private String subject;
    private String content;
    private String author;
    private String nid;
}
