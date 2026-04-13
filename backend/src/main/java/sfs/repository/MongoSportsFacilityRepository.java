package sfs.repository;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import sfs.model.SportsFacility;

import java.util.List;
import java.util.Optional;

@Repository
public class MongoSportsFacilityRepository implements SportsFacilityRepository{

    private final MongoTemplate mongoTemplate;

    public MongoSportsFacilityRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public SportsFacility save(SportsFacility sportsFacility) {
        return mongoTemplate.save(sportsFacility, "facilities");
    }

    @Override
    public Optional<SportsFacility> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, SportsFacility.class, "facilities"));
    }

    @Override
    public List<SportsFacility> findAll() {
        return mongoTemplate.findAll(SportsFacility.class, "facilities");
    }

    @Override
    public void deleteById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, SportsFacility.class, "facilities");
    }
}
