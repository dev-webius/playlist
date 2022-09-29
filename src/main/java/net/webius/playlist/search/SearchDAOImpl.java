package net.webius.playlist.search;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("searchDAO")
public class SearchDAOImpl implements SearchDAO {
    private final SqlSessionTemplate sqlSession;

    private final String NAMESPACE = "net.webius.playlist.mapper.SearchMapper.";

    public SearchDAOImpl(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    public List<SearchVO> search(SearchQueryVO searchQueryVO) throws SearchException {
        try {
            return sqlSession.selectList(NAMESPACE + "Search", searchQueryVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SearchException("Search ERROR");
        }
    }

    public List<SearchVO> searchPlatform(SearchQueryVO searchQueryVO) throws SearchException {
        try {
            return sqlSession.selectList(NAMESPACE + "SearchPlatform", searchQueryVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SearchException("Search Platform ERROR");
        }
    }

    public int count(SearchCountVO searchCountVO) throws SearchException {
        try {
            return sqlSession.selectOne(NAMESPACE + "SearchCount", searchCountVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SearchException("Search Count ERROR");
        }
    }

    public int countPlatform(SearchCountVO searchCountVO) throws SearchException {
        try {
            return sqlSession.selectOne(NAMESPACE + "SearchPlatformCount", searchCountVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SearchException("Search Platform Count ERROR");
        }
    }
}
