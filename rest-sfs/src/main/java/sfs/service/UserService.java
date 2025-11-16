package sfs.service;

import sfs.model.User;
import sfs.rest.dto.CreateUserRequest;
import sfs.rest.dto.UpdateUserRequest;

import java.util.List;
import java.util.UUID;

// CRU + activate/deactivate + searching[ByLogin/fragmentOfLogin]
public interface UserService {
    User createUser(User user) throws Exception;
    User createUserFromDTO(CreateUserRequest request) throws Exception;
    User updateUser(String id, UpdateUserRequest request) throws Exception;
    User getUserById(String id) throws Exception;
    List<User> getAllUsers();
    User activateUser(String id) throws Exception;
    User deactivateUser(String id) throws Exception;
    User findUserByLogin(String login) throws Exception;
    List<User> findUserByLoginFragment(String loginFragment) throws Exception;
}
