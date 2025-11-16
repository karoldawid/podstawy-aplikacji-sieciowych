package sfs.rest;

import jakarta.validation.Valid;
import sfs.model.User;
import org.springframework.web.bind.annotation.*;
import sfs.rest.dto.CreateUserRequest;
import sfs.rest.dto.UpdateUserRequest;
import sfs.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    public final UserService userService;

    public UserRestController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id) throws Exception {
        return userService.getUserById(id);
    }

    @GetMapping()
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PutMapping("/activate/{id}")
    public User activateUser(@PathVariable UUID id) throws Exception {
        return userService.activateUser(id);
    }

    @PutMapping("/deactivate/{id}")
    public User deactivateUser(@PathVariable UUID id) throws Exception {
        return userService.deactivateUser(id);
    }

    @GetMapping("/search/contains")
    public List<User> findUserByLoginFragment(@RequestParam String loginFragment) throws Exception{
        return userService.findUserByLoginFragment(loginFragment);
    }

    @GetMapping("/search/exact")
    public User findUserByLogin(@RequestParam String login) throws Exception {
        return userService.findUserByLogin(login);
    }

    @PostMapping("/create")
    public User createUser(@Valid @RequestBody CreateUserRequest request) throws Exception {
        return userService.createUserFromDTO(request);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest request) throws Exception{
        return userService.updateUser(id, request);
    }

}
