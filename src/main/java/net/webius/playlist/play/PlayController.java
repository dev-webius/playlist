package net.webius.playlist.play;

import lombok.extern.slf4j.Slf4j;
import net.webius.playlist.login.UserVO;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Controller
public class PlayController {
    private final PlayServiceImpl playService;
    private final PlayCheckUtil playCheckUtil;

    public PlayController(PlayServiceImpl playService, PlayCheckUtil playCheckUtil) {
        this.playService = playService;
        this.playCheckUtil = playCheckUtil;
    }

    @GetMapping("/play")
    public String playList(@RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
                           @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                           HttpServletRequest request,
                           ModelMap modelMap) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        // 요청 필터링
        if (pageIndex <= 0 || pageSize <= 0) {
            modelMap.addAttribute("alert", new PlayAlertVO("error", "비정상 접근을 감지했습니다.", "/play"));
            return "/play/alert";
        }

        // 요청 수행
        try {
            PlayListViewVO listViewVO = new PlayListViewVO(userVO.getUID(), new PlayPageVO((pageIndex - 1) * pageSize, pageSize));

            // 페이지 인덱스 검증
            int listLength = playService.count(listViewVO);
            if (listLength <= 0) {
                // 플레이 리스트가 없는 경우
                modelMap.addAttribute("playList", new ArrayList<PlayListVO>());
                modelMap.addAttribute("page", new PlayPageVO(1, 1));
                return "/play/list";
            }
            int pageLength = (int) Math.ceil((double) listLength / pageSize);
            if (pageIndex > pageLength) {
                modelMap.addAttribute("alert", new PlayAlertVO("error", "페이지를 찾을 수 없습니다.", "/play"));
                return "/play/alert";
            }

            // 플레이 리스트 검색
            List<PlayListVO> list = playService.listPage(listViewVO);

            // 결과 저장 후 데이터 전송
            modelMap.addAttribute("playList", list);
            modelMap.addAttribute("page", new PlayPageVO(pageIndex, pageLength));
        } catch (PlayException e) {
            e.printStackTrace();
        }
        return "/play/list";
    }

    @GetMapping("/play/write")
    public String playListWrite() {
        return "/play/write";
    }

    @PostMapping("/play/write")
    public String playListWrite(@RequestParam("title") String title,
                                HttpServletRequest request,
                                ModelMap modelMap) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        try {
            // 데이터 수집
            String listIDX = playService.getLast(new PlayListViewVO(userVO.getUID())) + 1 + "";
            PlayListWriteVO listWriteVO = new PlayListWriteVO(userVO.getUID(), listIDX, title);

            // 데이터 검증
            PlayAlertVO alertVO = playCheckUtil.verifyWrite(listWriteVO);
            if (alertVO != null) {
                // 검증 오류가 발생한 경우
                modelMap.addAttribute("alert", alertVO);
                return "/play/alert";
            }

            // 플레이 리스트 추가
            if (playService.write(listWriteVO)) {
                // 정상적으로 추가된 경우 (새로 추가한 플레이 리스트로 이동)
                modelMap.addAttribute("alert", new PlayAlertVO("ok", "생성되었습니다.", "/play/" + listIDX));
            } else {
                // 오류가 발생한 경우
                modelMap.addAttribute("alert", new PlayAlertVO("warn", "예기치 못한 오류가 발생하였습니다."));
            }
        } catch (PlayException e) {
            e.printStackTrace();
        }
        return "/play/alert";
    }

    @GetMapping("/play/{listId}/edit")
    public String playListEdit(@PathVariable("listId") String listId,
                               HttpServletRequest request,
                               ModelMap modelMap) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        PlayListViewVO listViewVO = new PlayListViewVO(userVO.getUID(), listId);
        try {
            // 플레이 리스트가 없는 경우
            if (playService.getPID(listViewVO) == null) {
                modelMap.addAttribute("alert", new PlayAlertVO("error", "존재하지 않는 플레이리스트 입니다.", "/play"));
                return "/play/alert";
            }

            // 리스트 데이터 조회
            PlayListVO listVO = playService.list(listViewVO);
            
            // 리스트 데이터 저장
            modelMap.addAttribute("list", listVO);
        } catch (PlayException e) {
            e.printStackTrace();
        }
        return "/play/edit";
    }

    @PostMapping("/play/{listId}/edit")
    public String playListEdit(@PathVariable("listId") String listId,
                               @RequestParam("title") String title,
                               HttpServletRequest request,
                               ModelMap modelMap) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        PlayListViewVO listViewVO = new PlayListViewVO(userVO.getUID(), listId);
        try {
            // 플레이 리스트가 없는 경우
            if (playService.getPID(listViewVO) == null) {
                modelMap.addAttribute("alert", new PlayAlertVO("error", "존재하지 않는 플레이리스트 입니다.", "/play"));
                return "/play/alert";
            }

            PlayListEditVO listEditVO = new PlayListEditVO(userVO.getUID(), listId, title);
            PlayAlertVO alertVO = playCheckUtil.verifyWrite(listEditVO);
            if (alertVO != null) {
                // 검증 오류가 발생한 경우
                modelMap.addAttribute("alert", alertVO);
                return "/play/alert";
            }

            // 플레이 리스트 변경
            if (playService.edit(listEditVO)) {
                // 정상적으로 변경한 경우 (레이 리스트로 이동)
                modelMap.addAttribute("alert", new PlayAlertVO("ok", "수정되었습니다.", "/play/" + listId));
            } else {
                // 오류가 발생한 경우
                modelMap.addAttribute("alert", new PlayAlertVO("warn", "예기치 못한 오류가 발생하였습니다."));
            }
        } catch (PlayException e) {
            e.printStackTrace();
        }
        return "/play/alert";
    }

    @GetMapping("/play/{listId}/delete")
    public String playListDelete(@PathVariable("listId") String listId,
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

            PlayListDeleteVO listDeleteVO = new PlayListDeleteVO(pid);
            if (playService.delete(listDeleteVO)) {
                // 삭제 성공 시
                if (playService.update(playService.getAll(listDeleteVO), Integer.parseInt(listId))) {
                    // IDX 업데이트 성공
                    modelMap.addAttribute("alert", new PlayAlertVO("ok", "삭제되었습니다.", "/play"));
                } else {
                    // IDX 업데이트 실패
                    modelMap.addAttribute("alert", new PlayAlertVO("error", "데이터 반영 중 오류가 발생하였습니다.", "/play"));
                }
            } else {
                modelMap.addAttribute("alert", new PlayAlertVO("error", "예기치 못한 오류가 발생하였습니다.", "/play/" + listId));
            }
        } catch (PlayException e) {
            e.printStackTrace();
        }
        return "/play/alert";
    }

    @ResponseBody
    @PostMapping(value = "/play/verifyTitle")
    public String verifyId(@RequestParam(value = "listId", required = false) String listId,
                           @RequestParam("title") String title,
                           HttpServletRequest request) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        PlayListWriteVO listWriteVO = new PlayListWriteVO(userVO.getUID(), listId, title);
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            // 데이터 검증
            PlayAlertVO alertVO = playCheckUtil.verifyWrite(listWriteVO);
            if (alertVO == null) {
                // 문제가 없는 경우
                alertVO = new PlayAlertVO("ok");
            }
            map.put("result", alertVO);
            return objectMapper.writeValueAsString(map);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
