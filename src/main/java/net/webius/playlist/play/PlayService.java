package net.webius.playlist.play;

import java.util.List;

public interface PlayService {
    /* List View */
    public PlayListVO list(PlayListViewVO listViewVO) throws PlayException;
    public PlayListVO list(String pid) throws PlayException;
    public List<PlayListVO> listPage(PlayListViewVO listViewVO) throws PlayException;
    public List<PlayListVO> listAll(PlayListViewVO listViewVO) throws PlayException;
    public int count(PlayListViewVO listViewVO) throws PlayException;
    public int getLast(PlayListViewVO listViewVO) throws PlayException;
    public String getPID(PlayListViewVO listViewVO) throws PlayException;
    public String getTitle(PlayListViewVO listViewVO) throws PlayException;

    /* List Write */
    public Boolean hasTitle(PlayListWriteVO listWriteVO) throws PlayException;
    public Boolean write(PlayListWriteVO listWriteVO) throws PlayException;

    /* List Edit */
    public Boolean edit(PlayListEditVO listEditVO) throws PlayException;

    /* List Delete */
    public Boolean delete(PlayListDeleteVO listDeleteVO) throws PlayException;
    public List<PlayListVO> getAll(PlayListDeleteVO listDeleteVO) throws PlayException;
    public Boolean update(List<PlayListVO> playList, int idx) throws PlayException;
}
