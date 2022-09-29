package net.webius.playlist.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlayListEditVO {
    private String owner;
    private String idx;
    private String title;
}
