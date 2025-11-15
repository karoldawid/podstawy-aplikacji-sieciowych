package repository;

import model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user); // działa również jako UPDATE
    Optional<User> findById(UUID id);
    List<User> findAll();
    // póki co CRU bez D
    void deleteById(UUID id);
    Optional<User> findByLogin(String login);
    List<User> findByLoginFragment(String loginFragment);
}
