package repository;

import model.User;

import java.util.*;

public class InMemoryUserRepository implements UserRepository {
    private final HashMap<UUID, User> clients = new HashMap<>();
    // pod PAS rozważyć ConcurrentHashMap do obłsugi wielowątkowości

    @Override
    public User save(User user) {
        clients.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(clients.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(clients.values());
    }

    @Override
    public void deleteById(UUID id) {
        clients.remove(id);
    }
}
