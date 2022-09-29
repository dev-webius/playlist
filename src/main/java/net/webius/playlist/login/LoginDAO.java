package net.webius.playlist.login;

public interface LoginDAO {
    public UserVO login(LoginVO loginVO) throws LoginException;
    public UserVO loginK(LoginApiVO loginApiVO) throws LoginException;
    public UserVO loginN(LoginApiVO loginApiVO) throws LoginException;
    public UserVO loginG(LoginApiVO loginApiVO) throws LoginException;
    public LoginAllVO getUserAll(String uid) throws LoginException;
    public String getUserAlias(String uid) throws  LoginException;
}
