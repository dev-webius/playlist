package net.webius.playlist.main;

import java.util.List;

public interface MainService {
    public MainFeedVO getList(String uid) throws MainException;
    public List<MainFeedVO> getNode(String uid) throws MainException;
}
