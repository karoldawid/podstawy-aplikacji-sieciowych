package rest.dto;

public class CreateUserRequest {
    private String login;
    private String firstName;
    private String lastName;
    private String userType;

    public CreateUserRequest(String login, String firstName, String lastName, String userType) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
