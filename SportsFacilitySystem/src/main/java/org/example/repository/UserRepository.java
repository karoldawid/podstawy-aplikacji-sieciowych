package org.example.repository;

import org.example.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    List<User> findAll();
    void deleteById(UUID id);
    Optional<User> findByLogin(String login);
    List<User> findByLoginContaining(String loginFragment);
}
