package ir.oveissi.core.user;

public class User {
    private String username;
    private String name;
    private String accessToken;
    private String refreshToken;

    public User(String name, String username, String accessToken, String refreshToken) {
        this.name = name;
        this.username = username;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
