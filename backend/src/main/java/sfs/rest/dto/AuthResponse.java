package sfs.rest.dto;

public class AuthResponse {
    private String token;
    private String userId;
    private String role;
    private String login;

    public AuthResponse(String token, String userId, String role, String login) {
        this.token = token;
        this.userId = userId;
        this.role = role;
        this.login = login;
    }

    public String getToken() { return token; }
    public String getUserId() { return userId; }
    public String getRole() { return role; }
    public String getLogin() { return login; }
}