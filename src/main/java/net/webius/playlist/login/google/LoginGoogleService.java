package net.webius.playlist.login.google;

public interface LoginGoogleService {
    final String CLIENT_ID = "798886589884-k8b2af5k6ut7304rmfrjb5siemeasksj.apps.googleusercontent.com"; // REST API KEY
    final String CLIENT_SECRET = "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"; // SECRET KEY
    final String REDIRECT_URI = "/login/google.callback"; // REDIRECT URI

    public String init(String state, String fqdn);
    public LoginGoogleCodeVO request(String code, String fqdn);
    public String verify();
    public LoginGoogleRefreshTokenVO refresh(String refresh_token);
    public String user();
}
