package pl.sfs.web_sfs_client.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.sfs.web_sfs_client.dto.RentalListDto;
import pl.sfs.web_sfs_client.dto.RentalRequestDto;
import pl.sfs.web_sfs_client.service.SfsService;

@Controller
@RequestMapping("/rental")
public class RentalRequestController {

    private final SfsService sfsService;

    public RentalRequestController(SfsService sfsService) {
        this.sfsService = sfsService;
    }

    @GetMapping
    public String showRentalForm(Model model) {
        model.addAttribute("rentalDto", new RentalRequestDto());
        return "rental-form";
    }

    @PostMapping
    public String rentFacility(@Valid @ModelAttribute("rentalDto") RentalRequestDto requestDto,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "rental-form";
        }
        try {
            sfsService.rentFacility(requestDto);
            return "redirect:/rental?success";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Błąd wypożyczania (backend odrzucił żądanie).");
            return "rental-form";
        }
    }

    @GetMapping("/list")
    public String getRentals(@RequestParam(required = false) String clientId, Model model) {
        if (clientId != null && !clientId.isEmpty()) {
            try {
                RentalListDto[] rentals = sfsService.getRentalsForClient(clientId);
                model.addAttribute("rentals", rentals);
            } catch (Exception e) {
                model.addAttribute("errorMessage", "Nie znaleziono klienta lub błąd połączenia.");
            }
        }
        model.addAttribute("searchClientId", clientId);
        return "rental-list";
    }

    @PostMapping("/delete/{id}")
    public String deleteRental(@PathVariable String id, @RequestParam String clientId) {
        try {
            sfsService.deleteRental(id);
            return "redirect:/rental/list?clientId=" + clientId + "&deleted";
        } catch (Exception e) {
            return "redirect:/rental/list?clientId=" + clientId + "&error=Blad_usuniecia";
        }
    }
}