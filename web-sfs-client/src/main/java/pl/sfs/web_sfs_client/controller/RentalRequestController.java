package pl.sfs.web_sfs_client.controller;

import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import pl.sfs.web_sfs_client.dto.RentalRequestDto;

@Controller
@RequestMapping("/rental")
public class RentalRequestController {
    private final RestTemplate restTemplate;
    private final String BACKEND_URL = "http://localhost:8080/api/v1/rentals/rent";

    public RentalRequestController (RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String showRentalForm(Model model){
        model.addAttribute("rentalDto", new RentalRequestDto());
        return "rental-form";
    }

    @PostMapping
    public String rentFacility(@Valid @ModelAttribute("rentalDto") RentalRequestDto requestDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "rental-form";
        }

        try{
            restTemplate.postForEntity(BACKEND_URL,requestDto, Void.class);
            return "redirect:/rental?success";
        } catch (Exception e){
            model.addAttribute("errorMessage", "Błąd wypożyczania " + e.getMessage());
            return "rental-form";
        }

    }


}
