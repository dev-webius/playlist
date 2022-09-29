package net.webius.playlist.search;

import java.util.List;

public interface SearchDAO {
    public List<SearchVO> search(SearchQueryVO searchQueryVO) throws SearchException;
    public List<SearchVO> searchPlatform(SearchQueryVO searchQueryVO) throws SearchException;
    public int count(SearchCountVO searchCountVO) throws SearchException;
    public int countPlatform(SearchCountVO searchCountVO) throws SearchException;
}
