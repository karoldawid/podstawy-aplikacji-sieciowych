package sfs.repository;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import sfs.model.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MongoUserRepository implements UserRepository{

    private final MongoTemplate mongoTemplate;

    public MongoUserRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public User save(User user) {
        // Baza sama generuje id
        return mongoTemplate.save(user, "users");
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, User.class, "users"));
    }

    @Override
    public List<User> findAll() {
        return mongoTemplate.findAll(User.class, "users");
    }

    @Override
    public void deleteById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, User.class, "users");
    }

    @Override
    public Optional<User> findByLogin(String login) {
        Query query = new Query(Criteria.where("login").is(login));
        return Optional.ofNullable(mongoTemplate.findOne(query, User.class, "users"));
    }

    @Override
    public List<User> findByLoginFragment(String loginFragment) {
        Query query = new Query(Criteria.where("login").regex(loginFragment, "i"));
        return mongoTemplate.find(query, User.class, "users");
    }
}
