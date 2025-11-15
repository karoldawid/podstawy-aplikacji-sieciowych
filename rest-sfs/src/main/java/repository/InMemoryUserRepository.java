package repository;

import lombok.val;
import model.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final HashMap<UUID, User> users = new HashMap<>();
    // rozważyć ConcurrentHashMap do obsługi wielowątkowości

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteById(UUID id) {
        users.remove(id);
    }

    @Override
    public Optional<User> findByLogin(String login){
        return users.values().stream().filter(user -> user.getLogin().equals(login)).findFirst();
    }

    @Override
    public List<User> findByLoginFragment(String loginFragment){
        return users.values().stream().filter(user -> user.getLogin().contains(loginFragment)).collect(Collectors.toList());
    }

}
