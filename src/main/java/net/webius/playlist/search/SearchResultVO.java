package net.webius.playlist.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchResultVO {
    private String query;
    private int count;
    private int platform;
    private String platformName;
}
