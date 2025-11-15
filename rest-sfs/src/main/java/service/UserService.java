package service;

import model.User;
import rest.dto.CreateUserRequest;
import rest.dto.UpdateUserRequest;

import java.util.List;
import java.util.UUID;

// CRU + activate/deactivate + searching[ByLogin/fragmentOfLogin]
public interface UserService {
    User createUser(User user) throws Exception;
    User createUserFromDTO(CreateUserRequest request) throws Exception;
    User updateUser(UUID id, UpdateUserRequest request) throws Exception;
    User getUserById(UUID id) throws Exception;
    List<User> getAllUsers();
    User activateUser(UUID id) throws Exception;
    User deactivateUser(UUID id) throws Exception;
    User findUserByLogin(String login) throws Exception;
    List<User> findUserByLoginFragment(String loginFragment) throws Exception;
}
