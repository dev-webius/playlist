package net.webius.playlist.play;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("playDeleteDAO")
public class PlayDeleteDAOImpl implements PlayDeleteDAO {
    private final SqlSessionTemplate sqlSession;

    private final String NAMESPACE = "net.webius.playlist.mapper.PlayDeleteMapper.";

    public PlayDeleteDAOImpl(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    public int listDelete(PlayListDeleteVO listDeleteVO) throws PlayException {
        try {
            return sqlSession.delete(NAMESPACE + "PlayListDelete", listDeleteVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayList Delete ERROR");
        }
    }

    public List<PlayListVO> listGetAll(PlayListDeleteVO listDeleteVO) throws PlayException {
        try {
            return sqlSession.selectList(NAMESPACE + "PlayListGetAll", listDeleteVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayList Get All ERROR");
        }
    }

    public int listUpdate(PlayListVO listVO) throws PlayException {
        try {
            return sqlSession.update(NAMESPACE + "PlayListUpdate", listVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayList Update ERROR");
        }
    }

    public int nodeDelete(PlayNodeDeleteVO nodeDeleteVO) throws PlayException {
        try {
            return sqlSession.delete(NAMESPACE + "PlayNodeDelete", nodeDeleteVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayNode Delete ERROR");
        }
    }

    public List<PlayNodeVO> nodeGetAll(PlayNodeDeleteVO nodeDeleteVO) throws PlayException {
        try {
            return sqlSession.selectList(NAMESPACE + "PlayNodeGetAll", nodeDeleteVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayNode Get All ERROR");
        }
    }

    public int nodeUpdate(PlayNodeVO nodeVO) throws PlayException {
        try {
            return sqlSession.update(NAMESPACE + "PlayNodeUpdate", nodeVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayNode Update ERROR");
        }
    }
}
