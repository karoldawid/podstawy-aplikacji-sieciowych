//package sfs.repository;
//
//import sfs.model.User;
//import org.springframework.stereotype.Repository;
//
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.stream.Collectors;
//
//public class InMemoryUserRepository implements UserRepository {
//    private final ConcurrentHashMap<UUID, User> users = new ConcurrentHashMap<>();
//
//    @Override
//    public User save(User user) {
//        Optional<User> existing = findByLogin(user.getLogin());
//        if (existing.isPresent() && (user.getId() == null || !existing.get().getId().equals(user.getId()))) {
//            throw new RuntimeException("Login " + user.getLogin() + " już istnieje i jest zajęty.");
//        }
//        if (user.getId() == null)
//        {   // źródło danych dpowiedzialne za nadawanie wartości kluczy
//            user.setId(UUID.randomUUID());
//        }
//        users.put(user.getId(), user);
//        return user;
//    }
//
//    @Override
//    public Optional<User> findById(UUID id) {
//        return Optional.ofNullable(users.get(id));
//    }
//
//    @Override
//    public List<User> findAll() {
//        return new ArrayList<>(users.values());
//    }
//
//    @Override
//    public void deleteById(UUID id) {
//        users.remove(id);
//    }
//
//    @Override
//    public Optional<User> findByLogin(String login){
//        return users.values().stream().filter(user -> user.getLogin().equals(login)).findFirst();
//    }
//
//    @Override
//    public List<User> findByLoginFragment(String loginFragment){
//        return users.values().stream().filter(user -> user.getLogin().contains(loginFragment)).collect(Collectors.toList());
//    }
//
//}
