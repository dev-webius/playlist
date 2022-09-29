package net.webius.playlist.play;

import java.util.List;

public interface PlayNodeService {
    /* Node View */
    public PlayNodeVO node(PlayNodeViewVO nodeViewVO) throws PlayException;
    public PlayNodeVO node(String pbid) throws PlayException;
    public List<PlayNodeVO> nodePage(PlayNodeViewVO nodeViewVO) throws PlayException;
    public List<PlayNodeVO> nodeAll(PlayNodeViewVO nodeViewVO) throws PlayException;
    public List<PlayNodeVO> nodePlatform(PlayNodeViewVO nodeViewVO, int type) throws PlayException;
    public List<PlayNodeVO> nodeRandom(PlayNodeViewVO nodeViewVO) throws PlayException;
    public int count(PlayNodeViewVO nodeViewVO) throws PlayException;
    public int getLast(PlayNodeViewVO nodeViewVO) throws PlayException;
    public String getPBID(PlayNodeViewVO nodeViewVO) throws PlayException;
    public Boolean log(PlayLogVO playLogVO) throws PlayException;

    /* Node Write */
    public Boolean write(PlayNodeWriteVO nodeWriteVO) throws PlayException;

    /* Node Edit */
    public Boolean edit(PlayNodeEditVO nodeEditVO) throws PlayException;

    /* Node Delete */
    public Boolean delete(PlayNodeDeleteVO nodeDeleteVO) throws PlayException;
    public List<PlayNodeVO> getAll(PlayNodeDeleteVO nodeDeleteVO) throws PlayException;
    public Boolean update(List<PlayNodeVO> nodeList, int idx) throws PlayException;
}
