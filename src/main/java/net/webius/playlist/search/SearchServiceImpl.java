package net.webius.playlist.search;

import org.springframework.stereotype.Service;

import java.util.List;

@Service("searchService")
public class SearchServiceImpl implements SearchService {
    private final SearchDAOImpl searchDAO;

    public SearchServiceImpl(SearchDAOImpl searchDAO) {
        this.searchDAO = searchDAO;
    }

    public List<SearchVO> search(SearchQueryVO searchQueryVO) throws SearchException {
        return searchDAO.search(searchQueryVO);
    }

    public List<SearchVO> searchPlatform(SearchQueryVO searchQueryVO, int type) throws SearchException {
        searchQueryVO.setType(type);
        return searchDAO.searchPlatform(searchQueryVO);
    }

    public int count(SearchCountVO searchCountVO) throws SearchException {
        return searchDAO.count(searchCountVO);
    }

    public int countPlatform(SearchCountVO searchCountVO, int type) throws SearchException {
        searchCountVO.setType(type);
        return searchDAO.countPlatform(searchCountVO);
    }
}
