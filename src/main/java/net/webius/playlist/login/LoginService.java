package net.webius.playlist.login;

public interface LoginService {
    public UserVO doLogin(LoginVO loginVO) throws LoginException;
    public UserVO doLogin(LoginApiVO loginApiVO, String apiName) throws LoginException;
    public LoginAllVO getUser(String uid) throws LoginException;
    public String getUserAlias(String uid) throws LoginException;
}
