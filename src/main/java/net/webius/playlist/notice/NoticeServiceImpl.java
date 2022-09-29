package net.webius.playlist.notice;

import org.springframework.stereotype.Service;

import java.util.List;

@Service("noticeService")
public class NoticeServiceImpl implements NoticeService {
    private final NoticeDAOImpl noticeDAO;

    public NoticeServiceImpl(NoticeDAOImpl noticeDAO) {
        this.noticeDAO = noticeDAO;
    }

    public NoticeVO notice(NoticeViewVO noticeViewVO) throws NoticeException {
        return noticeDAO.notice(noticeViewVO);
    }

    public List<NoticeVO> noticePage(NoticePageVO noticePageVO) throws NoticeException {
        return noticeDAO.noticePage(new NoticeViewVO(noticePageVO));
    }

    public List<NoticeVO> noticeAll() throws NoticeException {
        return noticeDAO.noticeAll();
    }

    public int count() throws NoticeException {
        return noticeDAO.noticeCount();
    }

    public Boolean visit(String nid) throws NoticeException {
        return noticeDAO.noticeVisit(nid) > 0;
    }

    public Boolean write(NoticeWriteVO noticeWriteVO) throws NoticeException {
        return noticeDAO.noticeWrite(noticeWriteVO) > 0;
    }

    public Boolean edit(NoticeEditVO noticeEditVO) throws NoticeException {
        return noticeDAO.noticeEdit(noticeEditVO) > 0;
    }

    public Boolean delete(NoticeDeleteVO noticeDeleteVO) throws NoticeException {
        return noticeDAO.noticeDelete(noticeDeleteVO) > 0;
    }
}
