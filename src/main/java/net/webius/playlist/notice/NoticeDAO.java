package net.webius.playlist.notice;

import java.util.List;

public interface NoticeDAO {
    public NoticeVO notice(NoticeViewVO noticeViewVO) throws NoticeException;
    public List<NoticeVO> noticePage(NoticeViewVO noticeViewVO) throws NoticeException;
    public List<NoticeVO> noticeAll() throws NoticeException;
    public int noticeCount() throws NoticeException;
    public int noticeVisit(String nid) throws NoticeException;
    public int noticeWrite(NoticeWriteVO noticeWriteVO) throws NoticeException;
    public int noticeEdit(NoticeEditVO noticeEditVO) throws NoticeException;
    public int noticeDelete(NoticeDeleteVO noticeDeleteVO) throws NoticeException;
}
