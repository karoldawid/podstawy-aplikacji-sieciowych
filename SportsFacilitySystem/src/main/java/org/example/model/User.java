package org.example.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.UUID;

// Na PAS bedzie abstrakcyjny User, a dziewdziczyć będzie Admin, FacilityManager, Client
// Nie ma po co wsm robić dziedziczenia bo ich konstruktory nie będą sie różnic a ich prawa bedzie definować enum
public class User {
    private UUID id;
    private String firstName;
    private String lastName;
    private boolean isActive;
    @NotBlank(message = "Login jest wymagany")
    @Size(min = 3, max = 50, message = "Login musi miec od 3 do 50 znaków")
    private String login;
    @NotEmpty(message = "Rola użytkownika jest wymagana")
    private Role role;

    public User() {
    }

    public User(String firstName, String lastName, String login, Role role) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = true;
        this.login = login;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public String getLogin() {
        return login;
    }

    public Role getRole() {
        return role;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
