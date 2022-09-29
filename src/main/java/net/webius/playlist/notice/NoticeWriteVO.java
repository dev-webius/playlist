package net.webius.playlist.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NoticeWriteVO {
    private String subject;
    private String content;
    private String author;
}
