package pl.sfs.web_sfs_client.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClientRegistrationDto {

    @NotBlank
    @Size(min = 4, max = 20)
    private String login;

    @NotBlank
    @Size(min = 4, max = 20)
    private String firstName;

    @NotBlank
    @Size(min = 4, max = 20)
    private String lastName;
}
