package net.webius.playlist.main;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("mainDAO")
public class MainDAOImpl implements MainDAO {
    private final SqlSessionTemplate sqlSession;

    private final String NAMESPACE = "net.webius.playlist.mapper.MainMapper.";

    public MainDAOImpl(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    public MainFeedVO getList(String uid) throws MainException {
        try {
            return sqlSession.selectOne(NAMESPACE + "GetList", uid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MainException("Main Get List ERROR");
        }
    }

    public List<MainFeedVO> getNode(String uid) throws MainException {
        try {
            return sqlSession.selectList(NAMESPACE + "GetNode", uid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MainException("Main Get NodeList ERROR");
        }
    }
}
