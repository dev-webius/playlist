package net.webius.playlist.sign;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignVerifyVO {
    private String uid;
    private String id;
    private String alias;
}
