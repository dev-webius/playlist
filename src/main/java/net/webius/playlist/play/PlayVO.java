package net.webius.playlist.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlayVO {
    private PlayListVO list;
    private PlayNodeVO node;
}
