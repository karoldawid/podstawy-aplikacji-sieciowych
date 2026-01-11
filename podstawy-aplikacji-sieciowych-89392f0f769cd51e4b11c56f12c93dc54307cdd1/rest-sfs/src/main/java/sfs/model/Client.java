package sfs.model;

public class Client extends User{

    private String phoneNumber;

    public Client() {
    }

    public Client(String login, String firstName, String lastName, String phoneNumber) {
        super(login, firstName, lastName);
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
