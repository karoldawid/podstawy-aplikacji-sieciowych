package sfs.repository;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;
import sfs.model.SportsFacility;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MongoSportsFacilityRepository implements PanacheMongoRepository<SportsFacility> {

    public SportsFacility save(SportsFacility facility) {
        persistOrUpdate(facility);
        return facility;
    }

    public Optional<SportsFacility> findById(String id) {
        try {
            return find("_id", new ObjectId(id)).firstResultOptional();
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public List<SportsFacility> findAllFacilities() {
        return listAll();
    }

    public void deleteById(String id) {
        try {
            delete("_id", new ObjectId(id));
        } catch (IllegalArgumentException e) {}
    }
}
