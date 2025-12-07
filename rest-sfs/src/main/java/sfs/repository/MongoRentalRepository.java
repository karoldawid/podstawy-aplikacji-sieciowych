package sfs.repository;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;
import sfs.model.Rental;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MongoRentalRepository implements PanacheMongoRepository<Rental> {

    public Rental save(Rental rental) {
        persistOrUpdate(rental);
        return rental;
    }

    public Optional<Rental> findById(String id) {
        try {
            return find("_id", new ObjectId(id)).firstResultOptional();
        } catch (IllegalArgumentException e) { return Optional.empty(); }
    }

    public void deleteById(String id) {
        try {
            delete("_id", new ObjectId(id));
        } catch (IllegalArgumentException e) {}
    }

    public List<Rental> findByClientId(String clientId) {
        return find("clientId", clientId).list();
    }

    public List<Rental> findByFacilityId(String facilityId) {
        return find("facilityId", facilityId).list();
    }

    public List<Rental> findPastByClientId(String clientId, LocalDateTime now) {
        return find("clientId = ?1 and endTime != null and endTime < ?2", clientId, now).list();
    }

    public List<Rental> findCurrentByClientId(String clientId, LocalDateTime now) {
        return find("clientId = ?1 and (endTime = null or endTime > ?2)", clientId, now).list();
    }

    public List<Rental> findPastByFacilityId(String facilityId, LocalDateTime now) {
        return find("facilityId = ?1 and endTime != null and endTime < ?2", facilityId, now).list();
    }

    public List<Rental> findCurrentByFacilityId(String facilityId, LocalDateTime now) {
        return find("facilityId = ?1 and (endTime = null or endTime > ?2)", facilityId, now).list();
    }
}
