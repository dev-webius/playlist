package net.webius.playlist.play;

import java.util.List;

public interface PlayDeleteDAO {
    public int listDelete(PlayListDeleteVO listDeleteVO) throws PlayException;
    public List<PlayListVO> listGetAll(PlayListDeleteVO listDeleteVO) throws PlayException;
    public int listUpdate(PlayListVO listVO) throws PlayException;

    public int nodeDelete(PlayNodeDeleteVO nodeDeleteVO) throws PlayException;
    public List<PlayNodeVO> nodeGetAll(PlayNodeDeleteVO nodeDeleteVO) throws PlayException;
    public int nodeUpdate(PlayNodeVO nodeVO) throws PlayException;
}
