package net.webius.playlist.notice;

import net.webius.playlist.util.StringUtil;

public class NoticeCheckUtil {
    private final int SUBJECT_SIZE = 150;
    private final int CONTENT_SIZE = 65535;

    public NoticeAlertVO verifyWrite(NoticeWriteVO noticeWriteVO) {
        // 권한 확인
        if (!noticeWriteVO.getAuthor().equals("1")) {
            // 관리자가 아닌 경우
            return new NoticeAlertVO("warn", "권한이 없습니다.");
        }

        // 입력 데이터 유효 검증
        if (noticeWriteVO.getSubject().equals("")) {
            // 제목이 없는 경우
            return new NoticeAlertVO("warn", "제목을 입력해주세요.");
        } else if (!StringUtil.isLimit(noticeWriteVO.getSubject(), SUBJECT_SIZE)) {
            // 제목이 허용 바이트를 초과하는 경우
            return new NoticeAlertVO("warn", "제목이 너무 깁니다.");
        } else if (noticeWriteVO.getContent().equals("")) {
            // 내용이 없는 경우
            return new NoticeAlertVO("warn", "내용을 입력해주세요.");
        } else if (!StringUtil.isLimit(noticeWriteVO.getContent(), CONTENT_SIZE)) {
            // 내용이 허용 바이트를 초과하는 경우
            return new NoticeAlertVO("warn", "내용이 너무 깁니다.");
        }

        return null;
    }

    // NoticeEditVO -> NoticeWriteVO
    public NoticeAlertVO verifyEdit(NoticeEditVO noticeEditVO) {
        return verifyWrite(new NoticeWriteVO(noticeEditVO.getSubject(), noticeEditVO.getContent(), noticeEditVO.getAuthor()));
    }
}
