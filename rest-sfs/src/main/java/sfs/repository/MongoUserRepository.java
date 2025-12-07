package sfs.repository;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;
import sfs.model.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class MongoUserRepository implements PanacheMongoRepository<User> {

    public User save(User user) {
        persistOrUpdate(user);
        return user;
    }

    public Optional<User> findById(String id) {
        try {
            return find("_id", new ObjectId(id)).firstResultOptional();
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public List<User> findAllUsers() {
        return listAll();
    }

    public void deleteById(String id) {
        try {
            delete("_id", new ObjectId(id));
        } catch (IllegalArgumentException e) {
        }
    }

    public Optional<User> findByLogin(String login) {
        return find("login", login).firstResultOptional();
    }

    public List<User> findByLoginFragment(String loginFragment) {
        return find("login like ?1", loginFragment).list();
    }
}
