package pl.sfs.web_sfs_client.dto;


import lombok.Data;

@Data
public class ClientRegistrationDto {
    private String login;
    private String firstName;
    private String lastName;
}
