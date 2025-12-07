package sfs.service;

import sfs.exception.ResourceNotFoundException;
import sfs.model.Admin;
import sfs.model.Client;
import sfs.model.FacilityManager;
import sfs.model.User;
import org.springframework.stereotype.Service;
import sfs.repository.UserRepository;
import sfs.rest.dto.CreateAdminRequest;
import sfs.rest.dto.CreateClientRequest;
import sfs.rest.dto.CreateFacilityManagerRequest;
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
    public Client createClient(CreateClientRequest request) throws Exception {
        Client client = new Client();
        client.setLogin(request.getLogin());
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());

        return (Client) userRepository.save(client);
    }

    @Override
    public Admin createAdmin(CreateAdminRequest request) throws Exception {
        Admin admin = new Admin();
        admin.setLogin(request.getLogin());
        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());

        return (Admin) userRepository.save(admin);
    }

    @Override
    public FacilityManager createFacilityManager(CreateFacilityManagerRequest request) throws Exception {
        FacilityManager manager = new FacilityManager();
        manager.setLogin(request.getLogin());
        manager.setFirstName(request.getFirstName());
        manager.setLastName(request.getLastName());

        return (FacilityManager) userRepository.save(manager);
    }


    @Override
    public User updateUser(String id, UpdateUserRequest request) throws Exception {
        User user = getUserById(id);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return userRepository.save(user);
    }

    @Override
    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Nie znaleziona użytkownika o ID: " + id));
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
    public User findUserByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new ResourceNotFoundException("Użytkownik o loginie: " + login + " nie został odnaleziony."));
    }

    @Override
    public List<User> findUserByLoginFragment(String loginFragment) throws Exception {
        return userRepository.findByLoginFragment(loginFragment);
    }
}
