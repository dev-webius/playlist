package net.webius.playlist.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayListViewVO {
    private String owner;
    private String idx;
    private PlayPageVO page;

    public PlayListViewVO(String owner) {
        this.owner = owner;
    }

    public PlayListViewVO(String owner, String idx) {
        this.owner = owner;
        this.idx = idx;
    }

    public PlayListViewVO(String owner, PlayPageVO page) {
        this.owner = owner;
        this.page = page;
    }
}
