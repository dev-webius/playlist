package net.webius.playlist.play;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayNodeViewVO {
    private String pid;
    private String idx;
    private int type;
    private PlayPageVO page;

    public PlayNodeViewVO(String pid) {
        this.pid = pid;
    }

    public PlayNodeViewVO(String pid, String idx) {
        this.pid = pid;
        this.idx = idx;
    }

    public PlayNodeViewVO(String pid, PlayPageVO page) {
        this.pid = pid;
        this.page = page;
    }
}
