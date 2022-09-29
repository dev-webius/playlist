package net.webius.playlist.play;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("playViewDAO")
public class PlayViewDAOImpl implements PlayViewDAO {
    private final SqlSessionTemplate sqlSession;

    private final String NAMESPACE = "net.webius.playlist.mapper.PlayViewMapper.";

    public PlayViewDAOImpl(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    public PlayListVO list(PlayListViewVO listViewVO) throws PlayException {
        try {
            return sqlSession.selectOne(NAMESPACE + "PlayList", listViewVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayList View ERROR");
        }
    }

    public PlayListVO list(String pid) throws PlayException {
        try {
            return sqlSession.selectOne(NAMESPACE + "PlayListPID", pid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayList PID View ERROR");
        }
    }

    public List<PlayListVO> listPage(PlayListViewVO listViewVO) throws PlayException {
        try {
            return sqlSession.selectList(NAMESPACE + "PlayListPage", listViewVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayList View Page ERROR");
        }
    }

    public List<PlayListVO> listAll(PlayListViewVO listViewVO) throws PlayException {
        try {
            return sqlSession.selectList(NAMESPACE + "PlayListAll", listViewVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayList View All ERROR");
        }
    }

    public int listCount(PlayListViewVO listViewVO) throws PlayException {
        try {
            return sqlSession.selectOne(NAMESPACE + "PlayListCount", listViewVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayList View Count ERROR");
        }
    }

    public int listLast(PlayListViewVO listViewVO) throws PlayException {
        try {
            return sqlSession.selectOne(NAMESPACE + "PlayListLastIDX", listViewVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayList Last IDX ERROR");
        }
    }

    public PlayNodeVO node(PlayNodeViewVO nodeViewVO) throws PlayException {
        try {
            return sqlSession.selectOne(NAMESPACE + "PlayNode", nodeViewVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayNode View ERROR");
        }
    }

    public PlayNodeVO node(String pbid) throws PlayException {
        try {
            return sqlSession.selectOne(NAMESPACE + "PlayNodePBID", pbid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayNode PBID View ERROR");
        }
    }

    public List<PlayNodeVO> nodePage(PlayNodeViewVO nodeViewVO) throws PlayException {
        try {
            return sqlSession.selectList(NAMESPACE + "PlayNodePage", nodeViewVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayNode View Page ERROR");
        }
    }

    public List<PlayNodeVO> nodeAll(PlayNodeViewVO nodeViewVO) throws PlayException {
        try {
            return sqlSession.selectList(NAMESPACE + "PlayNodeAll", nodeViewVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayNode View All ERROR");
        }
    }

    public List<PlayNodeVO> nodePlatform(PlayNodeViewVO nodeViewVO) throws PlayException {
        try {
            return sqlSession.selectList(NAMESPACE + "PlayNodePlatform", nodeViewVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayNode View Platform ERROR");
        }
    }

    public List<PlayNodeVO> nodeRandom(PlayNodeViewVO nodeViewVO) throws PlayException {
        try {
            return sqlSession.selectList(NAMESPACE + "PlayNodeRandom", nodeViewVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayNode View Random ERROR");
        }
    }

    public int nodeCount(PlayNodeViewVO nodeViewVO) throws PlayException {
        try {
            return sqlSession.selectOne(NAMESPACE + "PlayNodeCount", nodeViewVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayNode View Count ERROR");
        }
    }

    public int nodeLast(PlayNodeViewVO nodeViewVO) throws PlayException {
        try {
            return sqlSession.selectOne(NAMESPACE + "PlayNodeLastIDX", nodeViewVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayNode Last IDX ERROR");
        }
    }

    public int viewLog(PlayLogVO playLogVO) throws PlayException {
        try {
            return sqlSession.insert(NAMESPACE + "PlayLog", playLogVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("Play Log ERROR");
        }
    }

    public String getListPID(PlayListViewVO listViewVO) throws PlayException {
        try {
            return sqlSession.selectOne(NAMESPACE + "PlayListGetPID", listViewVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayList Get PID ERROR");
        }
    }

    public String getNodePBID(PlayNodeViewVO nodeViewVO) throws PlayException {
        try {
            return sqlSession.selectOne(NAMESPACE + "PlayNodeGetPBID", nodeViewVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayNode Get PBID ERROR");
        }
    }

    public String getListTitle(PlayListViewVO listViewVO) throws PlayException {
        try {
            return sqlSession.selectOne(NAMESPACE + "PlayListGetTitle", listViewVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayList Get Title ERROR");
        }
    }
}
