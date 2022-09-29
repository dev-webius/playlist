package net.webius.playlist.search;

import lombok.extern.slf4j.Slf4j;
import net.webius.playlist.login.UserVO;
import net.webius.playlist.play.PlayAlertVO;
import net.webius.playlist.util.PlatformUtil;
import net.webius.playlist.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class SearchController {
    private final SearchServiceImpl searchService;
    private final PlatformUtil platformUtil;

    public SearchController(SearchServiceImpl searchService, PlatformUtil platformUtil) {
        this.searchService = searchService;
        this.platformUtil = platformUtil;
    }

    @GetMapping("/search")
    public String searchQuery(@RequestParam("q") String query,
                              @RequestParam(value = "c", defaultValue = "all") String platform,
                              @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
                              @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                              HttpServletRequest request,
                              ModelMap modelMap) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        // 요청 필터링
        if (pageIndex <= 0 || pageSize <= 0) {
            modelMap.addAttribute("alert", new SearchAlertVO("error", "비정상 접근을 감지했습니다.", "/"));
            return "/search/alert";
        }

        if (!platform.equals("all") && platformUtil.getPlatformCode(platform) <= 0) {
            modelMap.addAttribute("alert", new SearchAlertVO("warn", "카테고리를 찾을 수 없습니다."));
            return "/search/alert";
        }

        String searchQuery;
        if (query.replaceAll(" ", "").equals("")) // 띄어쓰기 제외 쿼리가 없는 경우 공백으로 조회
            searchQuery = query;
        else
            searchQuery = "%" + query + "%";
        try {
            SearchCountVO searchCountVO = new SearchCountVO(userVO.getUID(), searchQuery);

            // 페이지 인덱스 검증
            int resultLength;
            if (platform.equals("all")) {
                resultLength = searchService.count(searchCountVO);
            } else {
                resultLength = searchService.countPlatform(searchCountVO, platformUtil.getPlatformCode(platform));
            }
            if (resultLength <= 0) {
                // 검색 결과가 없는 경우
                modelMap.addAttribute("resultList", new ArrayList<SearchVO>());
                modelMap.addAttribute("resultData", new SearchResultVO(StringUtil.pack(query), resultLength, platformUtil.getPlatformCode(platform), platform));
                modelMap.addAttribute("page", new SearchPageVO(1, 1));
                return "/search/result";
            }
            int pageLength = (int) Math.ceil((double) resultLength / pageSize);
            if (pageIndex > pageLength) {
                modelMap.addAttribute("alert", new PlayAlertVO("error", "페이지를 찾을 수 없습니다.", "/search?q=" + query));
                return "/search/alert";
            }

            // 검색 결과 수집
            SearchQueryVO searchQueryVO = new SearchQueryVO(userVO.getUID(), searchQuery, new SearchPageVO((pageIndex - 1) * pageSize, pageSize));
            List<SearchVO> result;
            if (platform.equals("all")) {
                result = searchService.search(searchQueryVO);
            } else {
                result = searchService.searchPlatform(searchQueryVO, platformUtil.getPlatformCode(platform));
            }

            // 결과 저장 후 데이터 전송
            modelMap.addAttribute("resultList", result);
            modelMap.addAttribute("resultData", new SearchResultVO(StringUtil.pack(query), resultLength, platformUtil.getPlatformCode(platform), platform));
            modelMap.addAttribute("page", new SearchPageVO(pageIndex, pageLength));
        } catch (SearchException e) {
            e.printStackTrace();
        }

        // 셔플/플랫폼 리스트 제거
        if (request.getSession().getAttribute("list") != null)
            request.getSession().removeAttribute("list");

        return "/search/result";
    }
}
