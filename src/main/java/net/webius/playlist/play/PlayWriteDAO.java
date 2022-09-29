package net.webius.playlist.play;

public interface PlayWriteDAO {
    public int listWrite(PlayListWriteVO listWriteVO) throws PlayException;
    public int hasListTitle(PlayListWriteVO listWriteVO) throws PlayException;
    public int nodeWrite(PlayNodeWriteVO nodeWriteVO) throws PlayException;
}
