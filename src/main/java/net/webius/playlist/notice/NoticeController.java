package net.webius.playlist.notice;

import lombok.extern.slf4j.Slf4j;
import net.webius.playlist.login.UserVO;
import net.webius.playlist.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class NoticeController {
    private final NoticeServiceImpl noticeService;
    private final NoticeCheckUtil noticeCheckUtil;

    public NoticeController(NoticeServiceImpl noticeService, NoticeCheckUtil noticeCheckUtil) {
        this.noticeService = noticeService;
        this.noticeCheckUtil = noticeCheckUtil;
    }

    @GetMapping("/notice")
    public String notice(@RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
                         @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                         ModelMap modelMap) {

        // 요청 필터링
        if (pageIndex <= 0 || pageSize <= 0) {
            modelMap.addAttribute("alert", new NoticeAlertVO("error", "비정상 접근을 감지했습니다.", "/notice"));
            return "/notice/alert";
        }

        try {
            // 페이지 인덱스 검증
            int listLength = noticeService.count();
            if (listLength <= 0) {
                modelMap.addAttribute("noticeList", new ArrayList<NoticeVO>());
                modelMap.addAttribute("page", new NoticePageVO(1, 1));
                return "/notice/board";
            }
            int pageLength = (int) Math.ceil((double) listLength / pageSize);
            if (pageIndex > pageLength) {
                modelMap.addAttribute("alert", new NoticeAlertVO("error", "페이지를 찾을 수 없습니다.", "/notice"));
                return "/notice/alert";
            }

            // 공지사항 리스트 검색
            List<NoticeVO> noticeList = noticeService.noticePage(new NoticePageVO((pageIndex - 1) * pageSize, pageSize));

            modelMap.addAttribute("noticeList", noticeList);
            modelMap.addAttribute("page", new NoticePageVO(pageIndex, pageLength));
        } catch (NoticeException e) {
            e.printStackTrace();
        }
        return "/notice/board";
    }

    @GetMapping("/notice/{noticeId}")
    public String noticeView(@PathVariable("noticeId") String noticeId, ModelMap modelMap) {
        NoticeViewVO noticeViewVO = new NoticeViewVO(noticeId);
        try {
            NoticeVO notice = noticeService.notice(noticeViewVO);
            if (notice == null) {
                modelMap.addAttribute("alert", new NoticeAlertVO("error", "게시글을 찾을 수 없습니다.", "/notice"));
                return "/notice/alert";
            }

            if (!noticeService.visit(notice.getNid())) {
                // 방문자 기록 중 문제가 발생한 경우
                modelMap.addAttribute("alert", new NoticeAlertVO("error", "예기치 못한 오류가 발생하였습니다. 일시적일 수 있으며, 관리자에게 문의 바랍니다.", "/notice"));
                return "/notice/alert";
            }

            notice.setView(Integer.parseInt(notice.getView()) + 1 + ""); // 방문자 Mismatch 문제 해결
            modelMap.addAttribute("notice", notice);
        } catch (NoticeException e) {
            e.printStackTrace();
        }
        return "/notice/view";
    }

    @GetMapping("/notice/write")
    public String noticeWrite(HttpServletRequest request,
                              ModelMap modelMap) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        if (userVO == null) {
            // 사용자 세션이 없는 경우
            modelMap.addAttribute("alert", new NoticeAlertVO("error", "비정상 접근을 감지했습니다.", "/notice"));
            return "/notice/alert";
        } else if (!userVO.getUID().equals("1")) {
            // 관리자가 아닌 경우 (관리자 UID = 1)
            modelMap.addAttribute("alert", new NoticeAlertVO("error", "권한이 없습니다.", "/notice"));
            return "/notice/alert";
        }
        return "/notice/write";
    }

    @PostMapping("/notice/write")
    public String noticeWrite(@RequestParam("subject") String subject,
                              @RequestParam("content") String content,
                              HttpServletRequest request,
                              ModelMap modelMap) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        if (userVO == null) {
            // 사용자 세션이 없는 경우
            modelMap.addAttribute("alert", new NoticeAlertVO("error", "비정상 접근을 감지했습니다.", "/notice"));
            return "/notice/alert";
        } else if (!userVO.getUID().equals("1")) {
            // 관리자가 아닌 경우 (관리자 UID = 1)
            modelMap.addAttribute("alert", new NoticeAlertVO("error", "권한이 없습니다.", "/notice"));
            return "/notice/alert";
        }

        NoticeWriteVO noticeWriteVO = new NoticeWriteVO(StringUtil.pack(subject), StringUtil.pack(content), userVO.getUID());
        try {
            // 텍스트 검증 확인
            NoticeAlertVO alertVO = noticeCheckUtil.verifyWrite(noticeWriteVO);
            if (alertVO != null) {
                modelMap.addAttribute("alert", alertVO);
                return "/notice/alert";
            }

            // 게시글 작성
            if (noticeService.write(noticeWriteVO)) {
                // 작성에 성공한 경우
                modelMap.addAttribute("alert", new NoticeAlertVO("ok", "작성되었습니다.", "/notice"));
            } else {
                // 작성에 문제가 발생한 경우
                modelMap.addAttribute("alert", new NoticeAlertVO("warn", "예기치 못한 오류가 발생하였습니다."));
            }
        } catch (NoticeException e) {
            e.printStackTrace();
        }
        return "/notice/alert";
    }

    @GetMapping("/notice/{noticeId}/edit")
    public String noticeEdit(@PathVariable("noticeId") String noticeId,
                             HttpServletRequest request,
                             ModelMap modelMap) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        if (userVO == null) {
            // 사용자 세션이 없는 경우
            modelMap.addAttribute("alert", new NoticeAlertVO("error", "비정상 접근을 감지했습니다.", "/notice"));
            return "/notice/alert";
        } else if (!userVO.getUID().equals("1")) {
            // 관리자가 아닌 경우 (관리자 UID = 1)
            modelMap.addAttribute("alert", new NoticeAlertVO("error", "권한이 없습니다.", "/notice"));
            return "/notice/alert";
        }

        NoticeViewVO noticeViewVO = new NoticeViewVO(noticeId);
        try {
            NoticeVO notice = noticeService.notice(noticeViewVO);
            if (notice == null) {
                modelMap.addAttribute("alert", new NoticeAlertVO("error", "게시글을 찾을 수 없습니다.", "/notice"));
                return "/notice/alert";
            }

            notice.setSubject(StringUtil.unpack(notice.getSubject()));
            notice.setContent(StringUtil.unpack(notice.getContent()));
            modelMap.addAttribute("notice", notice);
        } catch (NoticeException e) {
            e.printStackTrace();
        }
        return "/notice/edit";
    }

    @PostMapping("/notice/{noticeId}/edit")
    public String noticeEdit(@PathVariable("noticeId") String noticeId,
                             @RequestParam("subject") String subject,
                             @RequestParam("content") String content,
                             HttpServletRequest request,
                             ModelMap modelMap) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        if (userVO == null) {
            // 사용자 세션이 없는 경우
            modelMap.addAttribute("alert", new NoticeAlertVO("error", "비정상 접근을 감지했습니다.", "/notice"));
            return "/notice/alert";
        } else if (!userVO.getUID().equals("1")) {
            // 관리자가 아닌 경우 (관리자 UID = 1)
            modelMap.addAttribute("alert", new NoticeAlertVO("error", "권한이 없습니다.", "/notice"));
            return "/notice/alert";
        }

        // 게시글 존재 검증
        NoticeViewVO noticeViewVO = new NoticeViewVO(noticeId);
        NoticeVO notice = null;
        try {
            notice = noticeService.notice(noticeViewVO);
            if (notice == null) {
                // 게시글이 존재하지 않는 경우
                modelMap.addAttribute("alert", new NoticeAlertVO("error", "게시글을 찾을 수 없습니다.", "/notice"));
                return "/notice/alert";
            } else if (!notice.getAlias().equals(userVO.getALIAS())) {
                // 작성자 닉네임과 일치하지 않는 경우 (추후 게시판 구축 시 이러한 부분은 UID 검증으로 변경해야 함)
                modelMap.addAttribute("alert", new NoticeAlertVO("error", "권한이 없습니다.", "/notice"));
                return "/notice/alert";
            }
        } catch (NoticeException e) {
            e.printStackTrace();
        }

        NoticeEditVO noticeEditVO = new NoticeEditVO(StringUtil.pack(subject), StringUtil.pack(content), userVO.getUID(), notice.getNid());
        try {
            // 텍스트 검증 확인
            NoticeAlertVO alertVO = noticeCheckUtil.verifyEdit(noticeEditVO);
            if (alertVO != null) {
                modelMap.addAttribute("alert", alertVO);
                return "/notice/alert";
            }

            // 게시글 수정
            if (noticeService.edit(noticeEditVO)) {
                // 작성에 성공한 경우
                modelMap.addAttribute("alert", new NoticeAlertVO("ok", "수정되었습니다.", "/notice/" + notice.getNid()));
            } else {
                // 작성에 문제가 발생한 경우
                modelMap.addAttribute("alert", new NoticeAlertVO("warn", "예기치 못한 오류가 발생하였습니다."));
            }
        } catch (NoticeException e) {
            e.printStackTrace();
        }
        return "/notice/alert";
    }

    @GetMapping("/notice/{noticeId}/delete")
    public String noticeDelete(@PathVariable("noticeId") String noticeId,
                               HttpServletRequest request,
                               ModelMap modelMap) {

        UserVO userVO = (UserVO) request.getSession().getAttribute("user");
        if (userVO == null) {
            // 사용자 세션이 없는 경우
            modelMap.addAttribute("alert", new NoticeAlertVO("error", "비정상 접근을 감지했습니다.", "/notice"));
            return "/notice/alert";
        } else if (!userVO.getUID().equals("1")) {
            // 관리자가 아닌 경우 (관리자 UID = 1)
            modelMap.addAttribute("alert", new NoticeAlertVO("error", "권한이 없습니다.", "/notice"));
            return "/notice/alert";
        }

        NoticeViewVO noticeViewVO = new NoticeViewVO(noticeId);
        try {
            NoticeVO notice = noticeService.notice(noticeViewVO);
            if (notice == null) {
                modelMap.addAttribute("alert", new NoticeAlertVO("error", "게시글을 찾을 수 없습니다.", "/notice"));
                return "/notice/alert";
            }
        } catch (NoticeException e) {
            e.printStackTrace();
        }

        // 삭제 요청
        NoticeDeleteVO noticeDeleteVO = new NoticeDeleteVO(noticeId);
        try {
            if (noticeService.delete(noticeDeleteVO)) {
                // 정상적으로 삭제된 경우
                modelMap.addAttribute("alert", new NoticeAlertVO("ok", "삭제되었습니다.", "/notice"));
            } else {
                // 삭제에 오류가 발생한 경우
                modelMap.addAttribute("alert", new NoticeAlertVO("warn", "예기치 못한 오류가 발생하였습니다."));
            }
        } catch (NoticeException e) {
            e.printStackTrace();
        }
        return "/notice/alert";
    }
}
