package org.example.repository;

import org.example.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final HashMap<UUID, User> clients = new HashMap<>();
    // pod PAS rozważyć ConcurrentHashMap do obłsugi wielowątkowości

    @Override
    public User save(User user) {
        // Sprawdzenie unikalności (wymaganie biznesowe)
        // Sprawdzamy, czy istnieje INNY użytkownik (z innym ID), który ma już ten sam login
        boolean loginTaken = clients.values().stream()
                .anyMatch(existingUser -> existingUser.getLogin().equalsIgnoreCase(user.getLogin())
                        && !existingUser.getId().equals(user.getId()));
        if (loginTaken) {
            // Rzucamy wyjątek, który potem obsłużymy w kontrolerze jako błąd 400 (Bad Request)
            throw new IllegalArgumentException("Login '" + user.getLogin() + "' jest już zajęty.");
        }

        clients.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(clients.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(clients.values());
    }

    @Override
    public void deleteById(UUID id) {
        clients.remove(id);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return clients.values().stream()
                .filter(user -> user.getLogin().equalsIgnoreCase(login)) // ignorujemy wielkość liter
                .findFirst();
    }

    @Override
    public List<User> findByLoginContaining(String loginFragment) {
        String lowerCaseFragment = loginFragment.toLowerCase(); // Aby wyszukiwanie ignorowało wielkość liter
        return clients.values().stream()
                .filter(user -> user.getLogin().toLowerCase().contains(lowerCaseFragment))
                .collect(Collectors.toList());
        }

}
