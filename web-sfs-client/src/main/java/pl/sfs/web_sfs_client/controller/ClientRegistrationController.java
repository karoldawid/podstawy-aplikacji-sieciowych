package pl.sfs.web_sfs_client.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.sfs.web_sfs_client.dto.ClientRegistrationDto;
import pl.sfs.web_sfs_client.service.SfsService;

@Controller
@RequestMapping("/register")
public class ClientRegistrationController {

    private final SfsService sfsService;

    public ClientRegistrationController(SfsService sfsService) {
        this.sfsService = sfsService;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("clientDto", new ClientRegistrationDto());
        return "register-form";
    }

    @PostMapping
    public String registerClient(@Valid @ModelAttribute("clientDto") ClientRegistrationDto clientDto,
                                 BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register-form";
        }
        try {
            sfsService.registerClient(clientDto);
            return "redirect:/register?success";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Błąd rejestracji (login zajęty lub błąd serwera).");
            return "register-form";
        }
    }
}