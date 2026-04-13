package sfs.rest;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import sfs.model.User;
import org.springframework.web.bind.annotation.*;
import sfs.rest.dto.*;
import sfs.security.JwsService;
import sfs.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    public final UserService userService;
    private final JwsService jwsService;

    public UserRestController(UserService userService, JwsService jwsService){
        this.userService = userService;
        this.jwsService = jwsService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) throws Exception {
        User user = userService.getUserById(id);

        String signature = jwsService.createSignature(user.getId(), user.getVersion());

        return ResponseEntity.ok()
                .header("ETag", signature)
                .body(user);
    }

    @PutMapping("/{id}")
    public User updateUser(
            @PathVariable String id,
            @Valid @RequestBody UpdateUserRequest request,
            @RequestHeader(value = "If-Match", required = false) String ifMatch
    ) throws Exception{

        if (ifMatch == null || ifMatch.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Brak nagłówka If-Match. Edycja wymaga potwierdzenia wersji danych.");
        }

        User userFromDb = userService.getUserById(id);

        boolean isSignatureValid = jwsService.verifySignature(ifMatch, userFromDb.getId(), userFromDb.getVersion());

        if (!isSignatureValid) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Dane zostały zmienione w tle przez innego użytkownika. Odśwież stronę.");
        }

        return userService.updateUser(id, request);
    }


    @PostMapping("/change-password")
    public void changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.changePassword(login, request.getOldPassword(), request.getNewPassword());
    }

    @GetMapping()
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PutMapping("/{id}/activate")
    public User activateUser(@PathVariable String id) throws Exception {
        return userService.activateUser(id);
    }

    @PutMapping("/{id}/deactivate")
    public User deactivateUser(@PathVariable String id) throws Exception {
        return userService.deactivateUser(id);
    }

    // front realizuje po swojej stronie
    @GetMapping("/search/contains")
    public List<User> findUserByLoginFragment(@RequestParam String loginFragment) throws Exception{
        return userService.findUserByLoginFragment(loginFragment);
    }

    @GetMapping("/search/exact")
    public User findUserByLogin(@RequestParam String login) throws Exception {
        return userService.findUserByLogin(login);
    }
}