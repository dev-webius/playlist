package net.webius.playlist.play;

import org.springframework.stereotype.Service;

import java.util.List;

@Service("playService")
public class PlayServiceImpl implements PlayService {
    private final PlayViewDAOImpl playViewDAO;
    private final PlayWriteDAOImpl playWriteDAO;
    private final PlayEditDAOImpl playEditDAO;
    private final PlayDeleteDAOImpl playDeleteDAO;

    public PlayServiceImpl(PlayViewDAOImpl playViewDAO, PlayWriteDAOImpl playWriteDAO, PlayEditDAOImpl playEditDAO, PlayDeleteDAOImpl playDeleteDAO) {
        this.playViewDAO = playViewDAO;
        this.playWriteDAO = playWriteDAO;
        this.playEditDAO = playEditDAO;
        this.playDeleteDAO = playDeleteDAO;
    }

    public PlayListVO list(PlayListViewVO listViewVO) throws PlayException {
        return playViewDAO.list(listViewVO);
    }

    public PlayListVO list(String pid) throws PlayException {
        return playViewDAO.list(pid);
    }

    public List<PlayListVO> listPage(PlayListViewVO listViewVO) throws PlayException {
        return playViewDAO.listPage(listViewVO);
    }

    public List<PlayListVO> listAll(PlayListViewVO listViewVO) throws PlayException {
        return playViewDAO.listAll(listViewVO);
    }

    public int count(PlayListViewVO listViewVO) throws PlayException {
        return playViewDAO.listCount(listViewVO);
    }

    public int getLast(PlayListViewVO listViewVO) throws PlayException {
        return playViewDAO.listLast(listViewVO);
    }

    public String getPID(PlayListViewVO listViewVO) throws PlayException {
        return playViewDAO.getListPID(listViewVO);
    }

    public String getTitle(PlayListViewVO listViewVO) throws PlayException {
        return playViewDAO.getListTitle(listViewVO);
    }

    public Boolean hasTitle(PlayListWriteVO listWriteVO) throws PlayException {
        return playWriteDAO.hasListTitle(listWriteVO) > 0;
    }

    public Boolean write(PlayListWriteVO listWriteVO) throws PlayException {
        return playWriteDAO.listWrite(listWriteVO) > 0;
    }

    public Boolean edit(PlayListEditVO listEditVO) throws PlayException {
        return playEditDAO.listEdit(listEditVO) > 0;
    }

    public Boolean delete(PlayListDeleteVO listDeleteVO) throws PlayException {
        return playDeleteDAO.listDelete(listDeleteVO) > 0;
    }

    public List<PlayListVO> getAll(PlayListDeleteVO listDeleteVO) throws PlayException {
        return playDeleteDAO.listGetAll(listDeleteVO);
    }

    public Boolean update(List<PlayListVO> playList, int idx) throws PlayException {
        int listLength = playList.size();
        if (listLength <= 0) // 데이터가 없는 경우
            return true;

        int i, res = 0;
        for (i = 0; i < listLength; i++) {
            playList.get(i).setIdx(i + idx + "");
            res += playDeleteDAO.listUpdate(playList.get(i));
        }
        return listLength == res;
    }
}
