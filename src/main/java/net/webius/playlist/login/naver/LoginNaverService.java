package net.webius.playlist.login.naver;

public interface LoginNaverService {
    final String CLIENT_ID = "EKIS9CrtNLul9qNF_rdO"; // REST API KEY
    final String CLIENT_SECRET = "C0C6bpwBq8"; // SECRET KEY
    final String REDIRECT_URI = "/login/naver.callback"; // REDIRECT URI

    public String init(String state, String fqdn);
    public LoginNaverCodeVO request(String code, String fqdn);
    public String verify();
    public LoginNaverRefreshTokenVO refresh(String refresh_token);
    public String user();
}
