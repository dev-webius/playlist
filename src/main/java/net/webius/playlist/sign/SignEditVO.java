package net.webius.playlist.sign;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignEditVO {
    private String uid;
    private String pw;
    private String alias;

    public SignEditVO(String uid, String alias) {
        this.uid = uid;
        this.alias = alias;
    }
}
