package net.webius.playlist.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlayNodeEditVO {
    private String pid;
    private String idx;
    private String name;
    private String url;
    private String vid;
    private String thumb;
    private String type;
}
