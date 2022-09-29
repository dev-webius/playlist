package net.webius.playlist.sign;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignVO {
    private String UID;
    private String id;
    private String pw;
    private String alias;

    public SignVO(String id, String pw, String alias) {
        this.id = id;
        this.pw = pw;
        this.alias = alias;
    }

    public SignVO(String id, String alias) {
        this.id = id;
        this.alias = alias;
    }
}
