package net.webius.playlist.play;

public interface PlayEditDAO {
    public int listEdit(PlayListEditVO listEditVO) throws PlayException;
    public int nodeEdit(PlayNodeEditVO nodeEditVO) throws PlayException;
}
