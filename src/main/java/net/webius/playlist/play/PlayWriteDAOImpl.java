package net.webius.playlist.play;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository("playWriteDAO")
public class PlayWriteDAOImpl implements PlayWriteDAO {
    private final SqlSessionTemplate sqlSession;

    private final String NAMESPACE = "net.webius.playlist.mapper.PlayWriteMapper.";

    public PlayWriteDAOImpl(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    public int listWrite(PlayListWriteVO listWriteVO) throws PlayException {
        try {
            return sqlSession.insert(NAMESPACE + "PlayListWrite", listWriteVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayList Write ERROR");
        }
    }

    public int hasListTitle(PlayListWriteVO listWriteVO) throws PlayException {
        try {
            return sqlSession.selectOne(NAMESPACE + "PlayListHasTitle", listWriteVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayList Title Verify ERROR");
        }
    }

    public int nodeWrite(PlayNodeWriteVO nodeWriteVO) throws PlayException {
        try {
            return sqlSession.insert(NAMESPACE + "PlayNodeWrite", nodeWriteVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayNode Write ERROR");
        }
    }
}
