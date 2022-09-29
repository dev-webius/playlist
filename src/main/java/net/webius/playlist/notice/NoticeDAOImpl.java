package net.webius.playlist.notice;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("noticeDAO")
public class NoticeDAOImpl implements NoticeDAO {
    private final SqlSessionTemplate sqlSession;

    private final String NAMESPACE = "net.webius.playlist.mapper.NoticeMapper.";

    public NoticeDAOImpl(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    public NoticeVO notice(NoticeViewVO noticeViewVO) throws NoticeException {
        try {
            return sqlSession.selectOne(NAMESPACE + "Notice", noticeViewVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NoticeException("Notice View ERROR");
        }
    }

    public List<NoticeVO> noticePage(NoticeViewVO noticeViewVO) throws NoticeException {
        try {
            return sqlSession.selectList(NAMESPACE + "NoticeList", noticeViewVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NoticeException("Notice View Page ERROR");
        }
    }

    public List<NoticeVO> noticeAll() throws NoticeException {
        try {
            return sqlSession.selectList(NAMESPACE + "NoticeAll");
        } catch (Exception e) {
            e.printStackTrace();
            throw new NoticeException("Notice View All ERROR");
        }
    }

    public int noticeCount() throws NoticeException {
        try {
            return sqlSession.selectOne(NAMESPACE + "NoticeCount");
        } catch (Exception e) {
            e.printStackTrace();
            throw new NoticeException("Notice Count ERROR");
        }
    }

    public int noticeVisit(String nid) throws NoticeException {
        try {
            return sqlSession.update(NAMESPACE + "NoticeVisit", nid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NoticeException("Notice Visit ERROR");
        }
    }

    public int noticeWrite(NoticeWriteVO noticeWriteVO) throws NoticeException {
        try {
            return sqlSession.insert(NAMESPACE + "NoticeWrite", noticeWriteVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NoticeException("Notice Write ERROR");
        }
    }

    public int noticeEdit(NoticeEditVO noticeEditVO) throws NoticeException {
        try {
            return sqlSession.update(NAMESPACE + "NoticeEdit", noticeEditVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NoticeException("Notice Edit ERROR");
        }
    }

    public int noticeDelete(NoticeDeleteVO noticeDeleteVO) throws NoticeException {
        try {
            return sqlSession.delete(NAMESPACE + "NoticeDelete", noticeDeleteVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NoticeException("Notice Delete ERROR");
        }
    }
}
