package net.webius.playlist.search;

import java.util.List;

public interface SearchService {
    public List<SearchVO> search(SearchQueryVO searchQueryVO) throws SearchException;
    public List<SearchVO> searchPlatform(SearchQueryVO searchQueryVO, int type) throws SearchException;
    public int count(SearchCountVO searchCountVO) throws SearchException;
    public int countPlatform(SearchCountVO searchCountVO, int type) throws SearchException;
}
