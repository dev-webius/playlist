package net.webius.playlist.login;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository("loginDAO")
public class LoginDAOImpl implements LoginDAO {
    private final SqlSessionTemplate sqlSession;

    private final String NAMESPACE = "net.webius.playlist.mapper.LoginMapper.";

    public LoginDAOImpl(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    public UserVO login(LoginVO loginVO) throws LoginException {
        try {
            return sqlSession.selectOne(NAMESPACE + "Login", loginVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LoginException("Login Error :: Exception");
        }
    }

    public UserVO loginK(LoginApiVO loginApiVO) throws LoginException {
        try {
            return sqlSession.selectOne(NAMESPACE + "LoginKakao", loginApiVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LoginException("Login Kakao Error :: Exception");
        }
    }

    public UserVO loginN(LoginApiVO loginApiVO) throws LoginException {
        try {
            return sqlSession.selectOne(NAMESPACE + "LoginNaver", loginApiVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LoginException("Login Naver Error :: Exception");
        }
    }

    public UserVO loginG(LoginApiVO loginApiVO) throws LoginException {
        try {
            return sqlSession.selectOne(NAMESPACE + "LoginGoogle", loginApiVO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LoginException("Login Google Error :: Exception");
        }
    }

    public LoginAllVO getUserAll(String uid) throws LoginException {
        try {
            return sqlSession.selectOne(NAMESPACE + "GetUserAll", uid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LoginException("Login Get User All :: Exception");
        }
    }

    public String getUserAlias(String uid) throws LoginException {
        try {
            return sqlSession.selectOne(NAMESPACE + "GetUserAlias", uid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LoginException("Login Get User Alias :: Exception");
        }
    }
}
