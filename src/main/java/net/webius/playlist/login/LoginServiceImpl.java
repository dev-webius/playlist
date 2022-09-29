package net.webius.playlist.login;

import org.springframework.stereotype.Service;

@Service("loginService")
public class LoginServiceImpl implements LoginService {
    private final LoginDAOImpl loginDAO;

    public LoginServiceImpl(LoginDAOImpl loginDAO) {
        this.loginDAO = loginDAO;
    }

    public UserVO doLogin(LoginVO loginVO) throws LoginException {
        return loginDAO.login(loginVO);
    }

    public UserVO doLogin(LoginApiVO loginApiVO, String apiName) throws LoginException {
        if (apiName.equals("kakao"))
            return loginDAO.loginK(loginApiVO);
        if (apiName.equals("naver"))
            return loginDAO.loginN(loginApiVO);
        if (apiName.equals("google"))
            return loginDAO.loginG(loginApiVO);
        return null;
    }

    public LoginAllVO getUser(String uid) throws LoginException {
        return loginDAO.getUserAll(uid);
    }

    public String getUserAlias(String uid) throws LoginException {
        return loginDAO.getUserAlias(uid);
    }
}
