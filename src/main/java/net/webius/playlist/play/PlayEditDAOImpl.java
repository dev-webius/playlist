package net.webius.playlist.play;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository("playEditDAO")
public class PlayEditDAOImpl implements PlayEditDAO {
    private final SqlSessionTemplate sqlSession;

    private final String NAMESPACE = "net.webius.playlist.mapper.PlayEditMapper.";

    public PlayEditDAOImpl(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    public int listEdit(PlayListEditVO listEditVO) throws PlayException {
        try {
            return sqlSession.update(NAMESPACE + "PlayListEdit", listEditVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayList Edit ERROR");
        }
    }

    public int nodeEdit(PlayNodeEditVO nodeEditVO) throws PlayException {
        try {
            return sqlSession.update(NAMESPACE + "PlayNodeEdit", nodeEditVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PlayException("PlayNode Edit ERROR");
        }
    }
}
