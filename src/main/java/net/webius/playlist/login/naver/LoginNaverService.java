package net.webius.playlist.login.naver;

public interface LoginNaverService {
    final String CLIENT_ID = "yJT_xLxy6bTw1RM4IEY4"; // REST API KEY
    final String CLIENT_SECRET = "t2yMNau9UD"; // SECRET KEY
    final String REDIRECT_URI = "/login/naver.callback"; // REDIRECT URI

    public String init(String state, String fqdn);
    public LoginNaverCodeVO request(String code, String fqdn);
    public String verify();
    public LoginNaverRefreshTokenVO refresh(String refresh_token);
    public String user();
}
