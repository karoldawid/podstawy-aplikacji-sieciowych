package org.example.controller;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController // Mówi Springowi, że to jest Menedżer (Kontroler REST)
@RequestMapping("/api/users") // Wszystkie adresy w tej klasie będą zaczynać się od /api/users
public class UserController {

    // Nasza warstwa danych (repozytorium)
    private final UserRepository userRepository;

    // Spring automatycznie wstrzyknie tu komponent UserRepository
    // ponieważ oznaczyliśmy InMemoryUserRepository jako @Repository
    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Endpoint do pobierania listy wszystkich użytkowników.
     * Adres: GET http://localhost:8080/api/users
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Endpoint do pobierania jednego użytkownika po jego ID.
     * Adres: GET http://localhost:8080/api/users/...(jakieś UUID)...
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id) {
        // @PathVariable mówi Springowi, żeby wziął {id} z adresu
        // Na razie prosta obsługa, potem dodamy obsługę błędu 404
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Endpoint do tworzenia nowego użytkownika.
     * Adres: POST http://localhost:8080/api/users
     * Body (JSON): { "login": "nowy_user", "role": "CLIENT", ... }
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        // @RequestBody mówi Springowi, żeby wziął JSON-a
        // wysłanego przez klienta i zamienił go na obiekt User

        // (Tu w przyszłości dodasz walidację, np. czy login jest unikalny)

        return userRepository.save(user);
    }
}