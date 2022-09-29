package net.webius.playlist.play;

import java.util.List;

public interface PlayViewDAO {
    public PlayListVO list(PlayListViewVO listViewVO) throws PlayException;
    public PlayListVO list(String pid) throws PlayException;
    public List<PlayListVO> listPage(PlayListViewVO listViewVO) throws PlayException;
    public List<PlayListVO> listAll(PlayListViewVO listViewVO) throws PlayException;
    public int listCount(PlayListViewVO listViewVO) throws PlayException;
    public int listLast(PlayListViewVO listViewVO) throws PlayException;

    public PlayNodeVO node(PlayNodeViewVO nodeViewVO) throws PlayException;
    public PlayNodeVO node(String pbid) throws PlayException;
    public List<PlayNodeVO> nodePage(PlayNodeViewVO nodeViewVO) throws PlayException;
    public List<PlayNodeVO> nodeAll(PlayNodeViewVO nodeViewVO) throws PlayException;
    public List<PlayNodeVO> nodePlatform(PlayNodeViewVO nodeViewVO) throws PlayException;
    public List<PlayNodeVO> nodeRandom(PlayNodeViewVO nodeViewVO) throws PlayException;
    public int nodeCount(PlayNodeViewVO nodeViewVO) throws PlayException;
    public int nodeLast(PlayNodeViewVO nodeViewVO) throws PlayException;
    public int viewLog(PlayLogVO playLogVO) throws PlayException;

    public String getListPID(PlayListViewVO listViewVO) throws PlayException;
    public String getNodePBID(PlayNodeViewVO nodeViewVO) throws PlayException;
    public String getListTitle(PlayListViewVO listViewVO) throws PlayException;
}
