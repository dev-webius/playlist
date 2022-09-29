package net.webius.playlist.notice;

import java.util.List;

public interface NoticeService {
    public NoticeVO notice(NoticeViewVO noticeViewVO) throws NoticeException;
    public List<NoticeVO> noticePage(NoticePageVO noticePageVO) throws NoticeException;
    public List<NoticeVO> noticeAll() throws NoticeException;
    public int count() throws NoticeException;
    public Boolean visit(String nid) throws NoticeException;
    public Boolean write(NoticeWriteVO noticeWriteVO) throws NoticeException;
    public Boolean edit(NoticeEditVO noticeEditVO) throws NoticeException;
    public Boolean delete(NoticeDeleteVO noticeDeleteVO) throws NoticeException;
}
