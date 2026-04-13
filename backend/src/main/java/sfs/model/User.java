package sfs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version; // <--- WAŻNY IMPORT DLA MONGODB
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public abstract class User {
    @Id
    private String id;

    // pole pod etag
    @Version
    private Long version;

    private String firstName;
    private String lastName;
    private boolean isActive;
    private String login;

    @NotBlank
    @JsonIgnore // pole mpomijane przy wysylaniu danych do klienta
    private String password;

    public User(String login, String firstName, String lastName) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = true;
    }

    public User() {
    }


    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRole() {
        return this.getClass().getSimpleName().toUpperCase();
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isActive=" + isActive +
                ", version=" + version +
                '}';
    }
}