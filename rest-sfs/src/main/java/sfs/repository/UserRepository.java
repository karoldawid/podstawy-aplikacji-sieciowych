package sfs.repository;

import sfs.model.User;

import java.util.List;
import java.util.Optional;


public interface UserRepository {
    User save(User user);
    Optional<User> findById(String id);
    List<User> findAll();
    void deleteById(String id);
    Optional<User> findByLogin(String login);
    List<User> findByLoginFragment(String loginFragment);
}
