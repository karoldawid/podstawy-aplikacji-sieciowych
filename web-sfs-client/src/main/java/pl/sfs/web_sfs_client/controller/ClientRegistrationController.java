package pl.sfs.web_sfs_client.controller;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import pl.sfs.web_sfs_client.dto.ClientRegistrationDto;

@Controller
@RequestMapping("/register")
public class ClientRegistrationController {

    private final RestTemplate restTemplate;
    private final String BACKEND_URL = "http://localhost:8080/api/v1/clients";

    public ClientRegistrationController(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String showRegistrationForm(Model model){
        model.addAttribute("clientDto", new ClientRegistrationDto());
        return "register-form";
    }

    @PostMapping
    public String registerClient(@ModelAttribute("clientDto") ClientRegistrationDto clientDto, Model model){
        try{
            restTemplate.postForEntity(BACKEND_URL,clientDto, Void.class);
            return "redirect:/register?success";
        } catch (Exception e){
            model.addAttribute("errorMessage", "Błąd rejestracji: " + e.getMessage());
            return "register-form";
        }
    }



}
