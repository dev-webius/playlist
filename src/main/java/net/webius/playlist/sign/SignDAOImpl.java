package net.webius.playlist.sign;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository("signDAO")
public class SignDAOImpl implements SignDAO {
    private final SqlSessionTemplate sqlSession;

    private final String NAMESPACE = "net.webius.playlist.mapper.SignMapper.";

    public SignDAOImpl(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    public int sign(SignVO signVO) throws SignException {
        try {
            return sqlSession.insert(NAMESPACE + "Sign", signVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SignException("Sign Error :: Exception");
        }
    }

    public int signAuth(SignVO signVO) throws SignException {
        try {
            return sqlSession.insert(NAMESPACE + "SignAuth", signVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SignException("Sign Auth Error :: Exception");
        }
    }

    public int signKakao(SignVO signVO) throws SignException {
        try {
            return sqlSession.insert(NAMESPACE + "SignKakao", signVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SignException("Sign Kakao Error :: Exception");
        }
    }

    public int signNaver(SignVO signVO) throws SignException {
        try {
            return sqlSession.insert(NAMESPACE + "SignNaver", signVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SignException("Sign Naver Error :: Exception");
        }
    }

    public int signGoogle(SignVO signVO) throws SignException {
        try {
            return sqlSession.insert(NAMESPACE + "SignGoogle", signVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SignException("Sign Google Error :: Exception");
        }
    }

    public int edit(SignEditVO signEditVO) throws SignException {
        try {
            return sqlSession.update(NAMESPACE + "SignEdit", signEditVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SignException("Sign Edit Error :: Exception");
        }
    }

    public int editAuth(SignEditVO signEditVO) throws SignException {
        try {
            return sqlSession.update(NAMESPACE + "SignEditAuth", signEditVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SignException("Sign Edit Auth :: Exception");
        }
    }

    public int getId(SignVerifyVO verifyVO) throws SignException {
        try {
            return sqlSession.selectOne(NAMESPACE + "HasID", verifyVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SignException("Sign Get ID Error :: Exception");
        }
    }

    public int getAlias(SignVerifyVO verifyVO) throws SignException {
        try {
            return sqlSession.selectOne(NAMESPACE + "HasAlias", verifyVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SignException("Sign Get ID Error :: Exception");
        }
    }
}
