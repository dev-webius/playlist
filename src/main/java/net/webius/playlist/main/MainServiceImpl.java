package net.webius.playlist.main;

import org.springframework.stereotype.Service;

import java.util.List;

@Service("mainService")
public class MainServiceImpl implements MainService {
    private final MainDAOImpl mainDAO;

    public MainServiceImpl(MainDAOImpl mainDAO) {
        this.mainDAO = mainDAO;
    }

    public MainFeedVO getList(String uid) throws MainException {
        return mainDAO.getList(uid);
    }

    public List<MainFeedVO> getNode(String uid) throws MainException {
        return mainDAO.getNode(uid);
    }
}
