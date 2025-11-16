package sfs.service;

import sfs.model.Admin;
import sfs.model.Client;
import sfs.model.FacilityManager;
import sfs.model.User;
import org.springframework.stereotype.Service;
import sfs.repository.UserRepository;
import sfs.rest.dto.CreateUserRequest;
import sfs.rest.dto.UpdateUserRequest;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) throws Exception {
        return userRepository.save(user);
    }

    @Override
    public User createUserFromDTO(CreateUserRequest request) throws Exception {
        User newUser;
        String type = request.getUserType();

        if(type.equalsIgnoreCase("CLIENT")){
            newUser = new Client();
        } else if (type.equalsIgnoreCase("ADMIN")){
            newUser = new Admin();
        } else if (type.equalsIgnoreCase("MANAGER")){
            newUser = new FacilityManager();
        } else {
            throw new Exception("Nieznany typ użytkownika: " + request.getUserType());
        }

        newUser.setLogin(request.getLogin());
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());

        return userRepository.save(newUser);
    }


    @Override
    public User updateUser(String id, UpdateUserRequest request) throws Exception {
        User user = getUserById(id);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return userRepository.save(user);
    }

    @Override
    public User getUserById(String id) throws Exception {
        return userRepository.findById(id).orElseThrow(() -> new Exception("Nie znaleziona użytkownika o ID: " + id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User activateUser(String id) throws Exception {
        User user = getUserById(id);
        if(user.isActive()){
            throw new Exception("Użytkownik o ID: " + user.getId() + " już został aktywowany.");
        }
        user.setActive(true);
        return userRepository.save(user);
    }

    @Override
    public User deactivateUser(String id) throws Exception {
        User user = getUserById(id);
        if(!user.isActive()){
            throw new Exception("Użytkownik o ID: " + user.getId() + " już był nieaktywny.");
        }
        user.setActive(false);
        return userRepository.save(user);
    }

    @Override
    public User findUserByLogin(String login) throws Exception {
        return userRepository.findByLogin(login).orElseThrow(() -> new Exception("Użytkownik o loginie: " + login + " nie został odnaleziony."));
    }

    @Override
    public List<User> findUserByLoginFragment(String loginFragment) throws Exception {
        return userRepository.findByLoginFragment(loginFragment);
    }
}
