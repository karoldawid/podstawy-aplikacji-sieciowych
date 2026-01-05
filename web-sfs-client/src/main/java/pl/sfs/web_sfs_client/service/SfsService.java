package pl.sfs.web_sfs_client.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.sfs.web_sfs_client.dto.ClientRegistrationDto;
import pl.sfs.web_sfs_client.dto.RentalListDto;
import pl.sfs.web_sfs_client.dto.RentalRequestDto;

@Service
public class SfsService {

    private final RestTemplate restTemplate;

    private final String CLIENT_URL = "http://localhost:8080/api/v1/clients";
    private final String RENT_URL = "http://localhost:8080/api/v1/rentals/rent";
    private final String LIST_URL = "http://localhost:8080/api/v1/rentals/client/";
    private final String DELETE_URL = "http://localhost:8080/api/v1/rentals/";

    public SfsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void registerClient(ClientRegistrationDto dto) {
        restTemplate.postForEntity(CLIENT_URL, dto, Void.class);
    }

    public void rentFacility(RentalRequestDto dto) {
        restTemplate.postForEntity(RENT_URL, dto, Void.class);
    }

    public RentalListDto[] getRentalsForClient(String clientId) {
        return restTemplate.getForObject(LIST_URL + clientId, RentalListDto[].class);
    }

    public void deleteRental(String id) {
        try {
            restTemplate.delete(DELETE_URL + id);
        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("Rekord " + id + " już nie istnieje - uznajemy za sukces.");
        }
    }
}