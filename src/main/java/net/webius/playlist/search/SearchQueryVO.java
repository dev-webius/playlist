package net.webius.playlist.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchQueryVO {
    private String owner;
    private String query;
    private int type;
    private SearchPageVO page;

    public SearchQueryVO(String owner, String query, SearchPageVO page) {
        this.owner = owner;
        this.query = query;
        this.page = page;
    }
}
