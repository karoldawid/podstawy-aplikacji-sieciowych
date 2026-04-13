package sfs.repository;

import sfs.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user); // działa również jako UPDATE
    Optional<User> findById(String id);
    List<User> findAll();
    // póki co CRU bez D
    void deleteById(String id);
    Optional<User> findByLogin(String login);
    List<User> findByLoginFragment(String loginFragment);
}
