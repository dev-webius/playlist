package net.webius.playlist.main;

import java.util.List;

public interface MainDAO {
    public MainFeedVO getList(String uid) throws MainException;
    public List<MainFeedVO> getNode(String uid) throws MainException;
}
