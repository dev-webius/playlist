package net.webius.playlist.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchCountVO {
    private String owner;
    private String query;
    private int type;

    public SearchCountVO(String owner, String query) {
        this.owner = owner;
        this.query = query;
    }
}
