package net.webius.playlist.play;

import org.springframework.stereotype.Service;

import java.util.List;

@Service("playNodeService")
public class PlayNodeServiceImpl implements PlayNodeService {
    private final PlayViewDAOImpl playViewDAO;
    private final PlayWriteDAOImpl playWriteDAO;
    private final PlayEditDAOImpl playEditDAO;
    private final PlayDeleteDAOImpl playDeleteDAO;

    public PlayNodeServiceImpl(PlayViewDAOImpl playViewDAO, PlayWriteDAOImpl playWriteDAO, PlayEditDAOImpl playEditDAO, PlayDeleteDAOImpl playDeleteDAO) {
        this.playViewDAO = playViewDAO;
        this.playWriteDAO = playWriteDAO;
        this.playEditDAO = playEditDAO;
        this.playDeleteDAO = playDeleteDAO;
    }

    public PlayNodeVO node(PlayNodeViewVO nodeViewVO) throws PlayException {
        return playViewDAO.node(nodeViewVO);
    }

    public PlayNodeVO node(String pbid) throws PlayException {
        return playViewDAO.node(pbid);
    }

    public List<PlayNodeVO> nodePage(PlayNodeViewVO nodeViewVO) throws PlayException {
        return playViewDAO.nodePage(nodeViewVO);
    }

    public List<PlayNodeVO> nodeAll(PlayNodeViewVO nodeViewVO) throws PlayException {
        return playViewDAO.nodeAll(nodeViewVO);
    }

    public List<PlayNodeVO> nodePlatform(PlayNodeViewVO nodeViewVO, int type) throws PlayException {
        nodeViewVO.setType(type);
        return playViewDAO.nodePlatform(nodeViewVO);
    }

    public List<PlayNodeVO> nodeRandom(PlayNodeViewVO nodeViewVO) throws PlayException {
        return playViewDAO.nodeRandom(nodeViewVO);
    }

    public int count(PlayNodeViewVO nodeViewVO) throws PlayException {
        return playViewDAO.nodeCount(nodeViewVO);
    }

    public int getLast(PlayNodeViewVO nodeViewVO) throws PlayException {
        return playViewDAO.nodeLast(nodeViewVO);
    }

    public String getPBID(PlayNodeViewVO nodeViewVO) throws PlayException {
        return playViewDAO.getNodePBID(nodeViewVO);
    }

    public Boolean log(PlayLogVO playLogVO) throws PlayException {
        return playViewDAO.viewLog(playLogVO) > 0;
    }

    public Boolean write(PlayNodeWriteVO nodeWriteVO) throws PlayException {
        return playWriteDAO.nodeWrite(nodeWriteVO) > 0;
    }

    public Boolean edit(PlayNodeEditVO nodeEditVO) throws PlayException {
        return playEditDAO.nodeEdit(nodeEditVO) > 0;
    }

    public Boolean delete(PlayNodeDeleteVO nodeDeleteVO) throws PlayException {
        return playDeleteDAO.nodeDelete(nodeDeleteVO) > 0;
    }

    public List<PlayNodeVO> getAll(PlayNodeDeleteVO nodeDeleteVO) throws PlayException {
        return playDeleteDAO.nodeGetAll(nodeDeleteVO);
    }

    public Boolean update(List<PlayNodeVO> nodeList, int idx) throws PlayException {
        int listLength = nodeList.size();
        if (listLength <= 0) // 데이터가 없는 경우
            return true;

        int i, res = 0;
        for (i = 0; i < listLength; i++) {
            nodeList.get(i).setIdx(i + idx + "");
            res += playDeleteDAO.nodeUpdate(nodeList.get(i));
        }
        return listLength == res;
    }
}
