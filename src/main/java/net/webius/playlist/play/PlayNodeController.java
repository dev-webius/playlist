package net.webius.playlist.play;

import lombok.extern.slf4j.Slf4j;
import net.webius.playlist.login.UserVO;
import net.webius.playlist.util.*;
import org.codehaus.jackson.JsonNode;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Controller
public class PlayNodeController {
    private final PlayServiceImpl playService;
    private final PlayNodeServiceImpl playNodeService;
    private final PlatformUtil platformUtil;
    private final HostConnectionUtil hostConnectionUtil;

    public PlayNodeController(PlayServiceImpl playService, PlayNodeServiceImpl playNodeService, PlatformUtil platformUtil, HostConnectionUtil hostConnectionUtil) {
        this.playService = playService;
        this.playNodeService = playNodeService;
        this.platformUtil = platformUtil;
        this.hostConnectionUtil = hostConnectionUtil;
    }

    @GetMapping("/play/{listId}")
    public String playNodeList(@PathVariable("listId") String listId,
                               @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
                               @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                               HttpServletRequest request,
                               ModelMap modelMap) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        PlayListViewVO listViewVO = new PlayListViewVO(userVO.getUID(), listId);
        String pid = null, title = null;
        try {
            pid = playService.getPID(listViewVO);
            // 플레이 리스트가 없는 경우
            if (pid == null) {
                modelMap.addAttribute("alert", new PlayAlertVO("error", "존재하지 않는 플레이리스트 입니다.", "/play"));
                return "/play/alert";
            }
            title = playService.getTitle(listViewVO);
        } catch (PlayException e) {
            e.printStackTrace();
        }

        // 요청 필터링
        if (pageIndex <= 0 || pageSize <= 0) {
            modelMap.addAttribute("alert", new PlayAlertVO("error", "비정상 접근을 감지했습니다.", "/play/" + listId));
            return "/play/alert";
        }

        // 요청 수행
        try {
            PlayNodeViewVO nodeViewVO = new PlayNodeViewVO(pid, new PlayPageVO((pageIndex - 1) * pageSize, pageSize));

            // 페이지 인덱스 검증
            int nodeLength = playNodeService.count(nodeViewVO);
            if (nodeLength <= 0) {
                // 플레이 리스트에 영상이 없는 경우
                modelMap.addAttribute("playNode", new ArrayList<PlayNodeVO>());
                modelMap.addAttribute("page", new PlayPageVO(1, 1));
                modelMap.addAttribute("playTitle", title);
                return "/play/nodeList";
            }
            int pageLength = (int) Math.ceil((double) nodeLength / pageSize);
            if (pageIndex > pageLength) {
                modelMap.addAttribute("alert", new PlayAlertVO("error", "페이지를 찾을 수 없습니다.", "/play"));
                return "/play/alert";
            }

            // 플레이 노드 검색
            List<PlayNodeVO> node = playNodeService.nodePage(nodeViewVO);

            // 결과 저장 후 데이터 전송
            modelMap.addAttribute("playNode", node);
            modelMap.addAttribute("page", new PlayPageVO(pageIndex, pageLength));
            modelMap.addAttribute("playTitle", title);
        } catch (PlayException e) {
            e.printStackTrace();
        }

        // 셔플/플랫폼 리스트 제거
        if (request.getSession().getAttribute("list") != null)
            request.getSession().removeAttribute("list");

        return "/play/nodeList";
    }

    @GetMapping("/play/{listId}/view")
    public String playNodeView(@PathVariable("listId") String listId,
                               HttpServletRequest request,
                               ModelMap modelMap) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        PlayListViewVO listViewVO = new PlayListViewVO(userVO.getUID(), listId);
        String pid;
        try {
            pid = playService.getPID(listViewVO);
            // 플레이 리스트가 없는 경우
            if (pid == null) {
                modelMap.addAttribute("alert", new PlayAlertVO("error", "존재하지 않는 플레이리스트 입니다.", "/play"));
                return "/play/alert";
            }
        } catch (PlayException e) {
            e.printStackTrace();
        }

        // 셔플/플랫폼 리스트 제거
        if (request.getSession().getAttribute("list") != null)
            request.getSession().removeAttribute("list");

        modelMap.addAttribute("alert", new PlayAlertVO("ok", null, "/play/" + listId + "/view/1"));
        return "/play/alert";
    }

    // Referer이 존재하지 않는 경우 (Interceptor로 부터 넘겨받아서 처리)
    // Referer이 존재하지 않으면 영상이 자동 재생되지 않는 문제 해결을 위해 삽입
    @GetMapping("/play/{listId}/view/{nodeId}/redirect")
    public String playNodeViewRedirect(@PathVariable("listId") String listId,
                                       @PathVariable("nodeId") String nodeId,
                                       HttpServletRequest request,
                                       ModelMap modelMap) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        PlayListViewVO listViewVO = new PlayListViewVO(userVO.getUID(), listId);
        String pid;
        try {
            pid = playService.getPID(listViewVO);
            // 플레이 리스트가 없는 경우
            if (pid == null) {
                modelMap.addAttribute("alert", new PlayAlertVO("error", "존재하지 않는 플레이리스트 입니다.", "/play"));
                return "/play/alert";
            }
        } catch (PlayException e) {
            e.printStackTrace();
        }
        modelMap.addAttribute("alert", new PlayAlertVO("ok", null, "/play/" + listId + "/view/" + nodeId));
        return "/play/alert";
    }

    @GetMapping("/play/{listId}/view/{nodeId}")
    public String playNodeView(@PathVariable("listId") String listId,
                               @PathVariable("nodeId") String nodeId,
                               HttpServletRequest request,
                               ModelMap modelMap) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        // 미리 저장된 목록이 존재하는지 수집
        List<PlayNodeVO> playList = (List<PlayNodeVO>) request.getSession().getAttribute("list");
        int cIdx = -1; // 현재 영상의 위치 저장
        if (playList != null) {
            cIdx = 0;
            for (PlayNodeVO node : playList) {
                if (node.getIdx().equals(nodeId)) {
                    break;
                }
                cIdx++;
            }
            if (cIdx == playList.size()) {
                // 미리 저장된 목록에 없는 영상 실행 시
                request.getSession().removeAttribute("list");
                playList = null;
            }
        }

        PlayListViewVO listViewVO = new PlayListViewVO(userVO.getUID(), listId);
        String pid;
        try {
            pid = playService.getPID(listViewVO);
            // 플레이 리스트가 없는 경우
            if (pid == null) {
                modelMap.addAttribute("alert", new PlayAlertVO("error", "존재하지 않는 플레이리스트 입니다.", "/play"));
                return "/play/alert";
            }

            // 페이지 접근을 위한 데이터 수집
            PlayNodeViewVO nodeViewVO = new PlayNodeViewVO(pid, nodeId);
            PlayNodeVO nodeVO = playNodeService.node(nodeViewVO);
            if (nodeVO == null) {
                // 노드를 찾을 수 없는 경우
                if (playList == null) {
                    // 저장된 리스트가 없는 경우 오류
                    modelMap.addAttribute("alert", new PlayAlertVO("error", "영상을 찾을 수 없습니다.", "/play/" + listId));
                } else {
                    // 저장된 리스트가 있는 경우 다음 영상으로 이동
                    modelMap.addAttribute("alert", new PlayAlertVO("error", null, "/play/" + listId + "/view/" + playList.get(cIdx + 1).getIdx()));
                }
                return "/play/alert";
            }
            String pbid = playNodeService.getPBID(nodeViewVO);

            // 200804 사용자 브라우저 검증 기능 추가
            String userAgent = request.getHeader("User-Agent").toLowerCase();
            String userAgentName;
            if (userAgent.matches(".*(nexus)+.*"))
                userAgentName = "tablet";
            else if (userAgent.matches(".*(android|iphone)+.*"))
                userAgentName = "mobile";
            else if (userAgent.matches(".*(macintosh)+.*"))
                userAgentName = "mac";
            else
                userAgentName = "pc";
            modelMap.addAttribute("agent", userAgentName);

            // 플랫폼 별 데이터 전달부 구현
            int platformCode = Integer.parseInt(nodeVO.getType());
            if (platformCode == 0) {
                // 플랫폼 코드가 0 (Unknown)인 경우
                modelMap.addAttribute("alert", new PlayAlertVO("error", "지원하지 않는 플랫폼 입니다.", "/play/" + listId));
                return "/play/alert";
            }
            String platform = platformUtil.getPlatformName(platformCode);
            if (platform.equals("Youtube")) {
                modelMap.addAttribute("play", new PlayYoutubeVO(platform, nodeVO.getVid()));
            } else if (platform.equals("Naver")) {
                if (userAgentName.equals("mobile")) {
                    String MOBILE_AGENT = "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Mobile Safari/537.36";
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("User-Agent", MOBILE_AGENT);
                    // 200804 모바일 (iPhone은 아직 JSON 방식으로 통신함 https://m.tv.naver.com/embed/)
                    // 아이폰에선 영상 자동 재생이 불가능함
                    // m.tv.naver.com -> tv.naver.com 문제를 해결하기 위해 MOBILE_AGENT 데이터 삽입
                    modelMap.addAttribute("play", new PlayNaverVO(platform, hostConnectionUtil.getNode(hostConnectionUtil.getJsValue(
                            hostConnectionUtil.get(platformUtil.getNaverUrl(nodeVO.getVid(), 3), new HashMap<String, String>(), map).parse(),"ghtPlayerInfo"
                    )).toString()));
                } else {
                    // JSON 형태로 받아오기 때문에 GetNode 처리 했으나, Key-Value 구조로 변경되어 GetNode 제거 후 GetJsValue만 사용
                    modelMap.addAttribute("play", new PlayNaverVO(platform, hostConnectionUtil.getJsValue(
                            hostConnectionUtil.get(platformUtil.getNaverUrl(nodeVO.getVid(), 1)).parse(),"ghtPlayerInfo"
                    )));
                }
            } else if (platform.equals("Kakao")) {
                // 데이터가 너무 많아 정보 수집에 문제가 발생함
                // 뒷 쪽에 있는 skipOnErrorOfAdContents 데이터부터 파싱하여 새로운 노드 생성
                String content = "{\"skipOnErrorOfAdContents" + hostConnectionUtil.get(platformUtil.getKakaoUrl(nodeVO.getVid(), 1)).body().split("skipOnErrorOfAdContents")[1];
                JsonNode jsonNode = hostConnectionUtil.getNode(content);
                modelMap.addAttribute("play", new PlayKakaoVO(platform, jsonNode.get("videoLocation").get("url").getTextValue()));
            }

            modelMap.addAttribute("video", nodeVO);
            if (playList == null) {
                // 저장된 리스트가 없는 경우 저장
                playList = playNodeService.nodeAll(new PlayNodeViewVO(pid));
                modelMap.addAttribute("nodeList", playList);
                cIdx = 0;
                for (PlayNodeVO node : playList) {
                    if (node.getIdx().equals(nodeId)) {
                        break;
                    }
                    cIdx++;
                }
            } else {
                // 저장된 리스트가 있는 경우 저장
                modelMap.addAttribute("nodeList", playList);
            }
            PlayNodeVO next = (cIdx + 1 < playList.size()) ? playList.get(cIdx + 1) : null; // 마지막 영상 검증
            if (next != null) {
                modelMap.addAttribute("next", next);
            }

            if (!playNodeService.log(new PlayLogVO(userVO.getUID(), pid, pbid))) {
                // 로그 기록 중 문제가 발생한 경우
                modelMap.addAttribute("alert", new PlayAlertVO("error", "예기치 못한 오류가 발생하였습니다. 관리자에게 문의 바랍니다.", "/play/" + listId));
                return "/play/alert";
            }
        } catch (PlayException e) {
            e.printStackTrace();
        } catch (HostConnectionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/play/play";
    }

    @GetMapping("/play/{listId}/write")
    public String playNodeWrite(@PathVariable("listId") String listId,
                                HttpServletRequest request,
                                ModelMap modelMap) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        PlayListViewVO listViewVO = new PlayListViewVO(userVO.getUID(), listId);
        String pid, title;
        try {
            pid = playService.getPID(listViewVO);
            // 플레이 리스트가 없는 경우
            if (pid == null) {
                modelMap.addAttribute("alert", new PlayAlertVO("error", "존재하지 않는 플레이리스트 입니다.", "/play"));
                return "/play/alert";
            }
            title = playService.getTitle(listViewVO);
            modelMap.addAttribute("playTitle", title);
        } catch (PlayException e) {
            e.printStackTrace();
        }
        return "/play/nodeWrite";
    }

    @PostMapping("/play/{listId}/write")
    public String playNodeWrite(@PathVariable("listId") String listId,
                                @RequestParam("name") String name,
                                @RequestParam("url") String url,
                                HttpServletRequest request,
                                ModelMap modelMap) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        PlayListViewVO listViewVO = new PlayListViewVO(userVO.getUID(), listId);
        String pid = null;
        try {
            pid = playService.getPID(listViewVO);
            // 플레이 리스트가 없는 경우
            if (pid == null) {
                modelMap.addAttribute("alert", new PlayAlertVO("error", "존재하지 않는 플레이리스트 입니다.", "/play"));
                return "/play/alert";
            }
        } catch (PlayException e) {
            e.printStackTrace();
        }

        // 사전 데이터 정의
        String platform = platformUtil.getPlatform(url);
        int platformCode = platformUtil.getPlatformCode(platform);
        if (platformCode <= 0) {
            modelMap.addAttribute("alert", new PlayAlertVO("warn", "지원하지 않는 플랫폼 입니다."));
            return "/play/alert";
        }

        try {
            // 데이터 수집
            String nodeIdx = playNodeService.getLast(new PlayNodeViewVO(pid)) + 1 + "";
            String videoId = platformUtil.getVideoId(platform, url);
            String dataUrl = null;
            if (platformCode == 1) {
                // 유튜브 제목 파싱을 위한 URL 저장
                dataUrl = platformUtil.getYoutubeUrl(videoId, 1);
            } else if (platformCode == 2) {
                // 네이버 제목 파싱을 위한 URL 저장
                dataUrl = platformUtil.getNaverUrl(videoId, 2);
            } else if (platformCode == 3) {
                // 카카오 제목 파싱을 위한 URL 저장
                dataUrl = platformUtil.getKakaoUrl(videoId, 2);
            }
            Document document = hostConnectionUtil.get(dataUrl).parse();
            String thumb = platformUtil.getThumbnail(document, platform);

            // 데이터 가공
            if (name.equals(""))
                name = StringUtil.pack(hostConnectionUtil.getMetaValue(document, "og:title"));
            else
                name = StringUtil.pack(name);
            if (thumb == null)
                thumb = platformUtil.getThumbnailVideoId(platform, videoId);

            // 데이터 필터링
            if (videoId == null) {
                modelMap.addAttribute("alert", new PlayAlertVO("warn", "URL이 올바르지 않습니다."));
                return "/play/alert";
            }
            if (thumb == null) {
                modelMap.addAttribute("alert", new PlayAlertVO("warn", "썸네일을 찾을 수 없습니다. 이는 일시적인 오류일 수 있습니다. 다시 시도해주세요."));
                return "/play/alert";
            }

            // 데이터 처리
            PlayNodeWriteVO nodeWriteVO = new PlayNodeWriteVO(pid, nodeIdx, name, url, videoId, thumb, platformCode + "");
            if (playNodeService.write(nodeWriteVO)) {
                // 정상적으로 동작한 경우
                modelMap.addAttribute("alert", new PlayAlertVO("ok", "추가되었습니다", "/play/" + listId));
            } else {
                // 오류가 발생한 경우
                modelMap.addAttribute("alert", new PlayAlertVO("warn", "예기치 못한 오류가 발생하였습니다."));
            }
        } catch (PlayException e) {
            e.printStackTrace();
        } catch (HostConnectionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/play/alert";
    }

    @GetMapping("/play/{listId}/edit/{nodeId}")
    public String playNodeEdit(@PathVariable("listId") String listId,
                               @PathVariable("nodeId") String nodeId,
                               HttpServletRequest request,
                               ModelMap modelMap) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        PlayListViewVO listViewVO = new PlayListViewVO(userVO.getUID(), listId);
        String pid;
        try {
            pid = playService.getPID(listViewVO);
            // 플레이 리스트가 없는 경우
            if (pid == null) {
                modelMap.addAttribute("alert", new PlayAlertVO("error", "존재하지 않는 플레이리스트 입니다.", "/play"));
                return "/play/alert";
            }

            // 변경 페이지 접근을 위한 데이터 수집
            PlayNodeVO nodeVO = playNodeService.node(new PlayNodeViewVO(pid, nodeId));
            if (nodeVO == null) {
                // 노드를 찾을 수 없는 경우
                modelMap.addAttribute("alert", new PlayAlertVO("error", "영상을 찾을 수 없습니다.", "/play/" + listId));
                return "/play/alert";
            } else {
                // 정상 수집된 경우
                modelMap.addAttribute("node", nodeVO);
            }
        } catch (PlayException e) {
            e.printStackTrace();
        }
        return "/play/nodeEdit";
    }

    @PostMapping("/play/{listId}/edit/{nodeId}")
    public String playNodeEdit(@PathVariable("listId") String listId,
                               @PathVariable("nodeId") String nodeId,
                               @RequestParam("name") String name,
                               @RequestParam("url") String url,
                               HttpServletRequest request,
                               ModelMap modelMap) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        PlayListViewVO listViewVO = new PlayListViewVO(userVO.getUID(), listId);
        String pid = null;
        try {
            pid = playService.getPID(listViewVO);
            // 플레이 리스트가 없는 경우
            if (pid == null) {
                modelMap.addAttribute("alert", new PlayAlertVO("error", "존재하지 않는 플레이리스트 입니다.", "/play"));
                return "/play/alert";
            }

            // 변경 페이지 접근을 위한 데이터 수집
            if (playNodeService.node(new PlayNodeViewVO(pid, nodeId)) == null) {
                // 노드를 찾을 수 없는 경우
                modelMap.addAttribute("alert", new PlayAlertVO("error", "영상을 찾을 수 없습니다.", "/play/" + listId));
                return "/play/alert";
            }
        } catch (PlayException e) {
            e.printStackTrace();
        }

        // 사전 데이터 정의
        String platform = platformUtil.getPlatform(url);
        int platformCode = platformUtil.getPlatformCode(platform);
        if (platformCode <= 0) {
            modelMap.addAttribute("alert", new PlayAlertVO("warn", "지원하지 않는 플랫폼 입니다."));
            return "/play/alert";
        }

        try {
            // 데이터 수집
            String videoId = platformUtil.getVideoId(platform, url);
            String dataUrl = null;
            if (platformCode == 1) {
                // 유튜브 제목 파싱을 위한 URL 저장
                dataUrl = platformUtil.getYoutubeUrl(videoId, 1);
            } else if (platformCode == 2) {
                // 네이버 제목 파싱을 위한 URL 저장
                dataUrl = platformUtil.getNaverUrl(videoId, 2);
            } else if (platformCode == 3) {
                // 카카오 제목 파싱을 위한 URL 저장
                dataUrl = platformUtil.getKakaoUrl(videoId, 2);
            }
            Document document = hostConnectionUtil.get(dataUrl).parse();
            String thumb = platformUtil.getThumbnail(document, platform);

            // 데이터 가공
            if (name.equals(""))
                name = StringUtil.pack(hostConnectionUtil.getMetaValue(document, "og:title"));
            else
                name = StringUtil.pack(name);
            if (thumb == null)
                thumb = platformUtil.getThumbnailVideoId(platform, videoId);

            // 데이터 필터링
            if (videoId == null) {
                modelMap.addAttribute("alert", new PlayAlertVO("warn", "URL이 올바르지 않습니다."));
                return "/play/alert";
            }
            if (thumb == null) {
                modelMap.addAttribute("alert", new PlayAlertVO("warn", "썸네일을 찾을 수 없습니다. 이는 일시적인 오류일 수 있습니다. 다시 시도해주세요."));
                return "/play/alert";
            }

            // 데이터 처리
            PlayNodeEditVO nodeEditVO = new PlayNodeEditVO(pid, nodeId, name, url, videoId, thumb, platformCode + "");
            if (playNodeService.edit(nodeEditVO)) {
                // 정상적으로 동작한 경우
                modelMap.addAttribute("alert", new PlayAlertVO("ok", "수정되었습니다.", "/play/" + listId));
            } else {
                // 오류가 발생한 경우
                modelMap.addAttribute("alert", new PlayAlertVO("warn", "예기치 못한 오류가 발생하였습니다."));
            }
        } catch (PlayException e) {
            e.printStackTrace();
        } catch (HostConnectionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/play/alert";
    }

    @GetMapping("/play/{listId}/delete/{nodeId}")
    public String playNodeDelete(@PathVariable("listId") String listId,
                                 @PathVariable("nodeId") String nodeId,
                                 HttpServletRequest request,
                                 ModelMap modelMap) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        PlayListViewVO listViewVO = new PlayListViewVO(userVO.getUID(), listId);
        try {
            // 플레이 리스트가 없는 경우
            String pid = playService.getPID(listViewVO);
            if (pid == null) {
                modelMap.addAttribute("alert", new PlayAlertVO("error", "존재하지 않는 플레이리스트 입니다.", "/play"));
                return "/play/alert";
            }

            // 변경 페이지 접근을 위한 데이터 수집
            if (playNodeService.node(new PlayNodeViewVO(pid, nodeId)) == null) {
                // 노드를 찾을 수 없는 경우
                modelMap.addAttribute("alert", new PlayAlertVO("error", "영상을 찾을 수 없습니다.", "/play/" + listId));
                return "/play/alert";
            }

            PlayNodeDeleteVO nodeDeleteVO = new PlayNodeDeleteVO(pid, nodeId);
            if (playNodeService.delete(nodeDeleteVO)) {
                // 삭제 성공 시
                if (playNodeService.update(playNodeService.getAll(nodeDeleteVO), Integer.parseInt(nodeId))) {
                    // IDX 업데이트 성공
                    modelMap.addAttribute("alert", new PlayAlertVO("ok", "삭제되었습니다.", "/play/" + listId));
                } else {
                    // IDX 업데이트 실패
                    modelMap.addAttribute("alert", new PlayAlertVO("error", "데이터 반영 중 오류가 발생하였습니다.", "/play/" + listId));
                }
            } else {
                modelMap.addAttribute("alert", new PlayAlertVO("error", "예기치 못한 오류가 발생하였습니다.", "/play/" + listId));
            }
        } catch (PlayException e) {
            e.printStackTrace();
        }
        return "/play/alert";
    }

    @GetMapping("/play/{listId}/surple")
    public ModelAndView playNodeViewSurple(@PathVariable("listId") String listId,
                                           HttpServletRequest request,
                                           ModelAndView modelAndView) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        try {
            // 플레이 리스트가 없는 경우
            String pid = playService.getPID(new PlayListViewVO(userVO.getUID(), listId));
            if (pid == null) {
                modelAndView.addObject("alert", new PlayAlertVO("error", "존재하지 않는 플레이리스트 입니다.", "/play"));
                modelAndView.setViewName("/play/alert");
                return modelAndView;
            }

            // 플레이 노드가 없는 경우
            List<PlayNodeVO> nodeList = playNodeService.nodeRandom(new PlayNodeViewVO(pid));
            if (nodeList.size() == 0) {
                modelAndView.addObject("alert", new PlayAlertVO("error", "영상이 없습니다.", "/play/" + listId));
                modelAndView.setViewName("/play/alert");
                return modelAndView;
            }

            request.getSession().setAttribute("list", nodeList);
            modelAndView.setView(MVCUtil.redirect("/play/" + listId + "/view/" + nodeList.get(0).getIdx()));
        } catch (PlayException e) {
            e.printStackTrace();
        }
        return modelAndView;
    }

    @GetMapping("/play/{listId}/youtube")
    public ModelAndView playNodeViewYoutube(@PathVariable("listId") String listId,
                                            HttpServletRequest request,
                                            ModelAndView modelAndView) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        try {
            // 플레이 리스트가 없는 경우
            String pid = playService.getPID(new PlayListViewVO(userVO.getUID(), listId));
            if (pid == null) {
                modelAndView.addObject("alert", new PlayAlertVO("error", "존재하지 않는 플레이리스트 입니다.", "/play"));
                modelAndView.setViewName("/play/alert");
                return modelAndView;
            }

            // 플레이 노드가 없는 경우
            List<PlayNodeVO> nodeList = playNodeService.nodePlatform(new PlayNodeViewVO(pid), 1);
            if (nodeList.size() == 0) {
                modelAndView.addObject("alert", new PlayAlertVO("error", "영상이 없습니다.", "/play/" + listId));
                modelAndView.setViewName("/play/alert");
                return modelAndView;
            }

            request.getSession().setAttribute("list", nodeList);
            modelAndView.setView(MVCUtil.redirect("/play/" + listId + "/view/" + nodeList.get(0).getIdx()));
        } catch (PlayException e) {
            e.printStackTrace();
        }
        return modelAndView;
    }

    @GetMapping("/play/{listId}/naver")
    public ModelAndView playNodeViewNaver(@PathVariable("listId") String listId,
                                          HttpServletRequest request,
                                          ModelAndView modelAndView) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        try {
            // 플레이 리스트가 없는 경우
            String pid = playService.getPID(new PlayListViewVO(userVO.getUID(), listId));
            if (pid == null) {
                modelAndView.addObject("alert", new PlayAlertVO("error", "존재하지 않는 플레이리스트 입니다.", "/play"));
                modelAndView.setViewName("/play/alert");
                return modelAndView;
            }

            // 플레이 노드가 없는 경우
            List<PlayNodeVO> nodeList = playNodeService.nodePlatform(new PlayNodeViewVO(pid), 2);
            if (nodeList.size() == 0) {
                modelAndView.addObject("alert", new PlayAlertVO("error", "영상이 없습니다.", "/play/" + listId));
                modelAndView.setViewName("/play/alert");
                return modelAndView;
            }

            request.getSession().setAttribute("list", nodeList);
            modelAndView.setView(MVCUtil.redirect("/play/" + listId + "/view/" + nodeList.get(0).getIdx()));
        } catch (PlayException e) {
            e.printStackTrace();
        }
        return modelAndView;
    }

    @GetMapping("/play/{listId}/kakao")
    public ModelAndView playNodeViewKakao(@PathVariable("listId") String listId,
                                          HttpServletRequest request,
                                          ModelAndView modelAndView) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        try {
            // 플레이 리스트가 없는 경우
            String pid = playService.getPID(new PlayListViewVO(userVO.getUID(), listId));
            if (pid == null) {
                modelAndView.addObject("alert", new PlayAlertVO("error", "존재하지 않는 플레이리스트 입니다.", "/play"));
                modelAndView.setViewName("/play/alert");
                return modelAndView;
            }

            // 플레이 노드가 없는 경우
            List<PlayNodeVO> nodeList = playNodeService.nodePlatform(new PlayNodeViewVO(pid), 3);
            if (nodeList.size() == 0) {
                modelAndView.addObject("alert", new PlayAlertVO("error", "영상이 없습니다.", "/play/" + listId));
                modelAndView.setViewName("/play/alert");
                return modelAndView;
            }

            request.getSession().setAttribute("list", nodeList);
            modelAndView.setView(MVCUtil.redirect("/play/" + listId + "/view/" + nodeList.get(0).getIdx()));
        } catch (PlayException e) {
            e.printStackTrace();
        }
        return modelAndView;
    }
}