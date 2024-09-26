package net.webius.playlist.login.kakao;

public interface LoginKakaoService {
    final String CLIENT_ID = "6f6ca4ac8a85f2b6b253308003efeec2"; // REST API KEY
    final String CLIENT_SECRET = "9a909c846d0fbe7bec98609240d43913"; // SECRET KEY
    final String REDIRECT_URI = "/login/kakao.callback"; // REDIRECT URI

    public String init(String state, String fqdn);
    public LoginKakaoCodeVO request(String code, String fqdn);
    public String verify();
    public LoginKakaoRefreshTokenVO refresh(String refresh_token);
    public String user();
}
