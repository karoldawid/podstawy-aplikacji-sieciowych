package sfs.repository;

import sfs.model.SportsFacility;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemorySportsFacilityRepository implements SportsFacilityRepository{
    private final ConcurrentHashMap<UUID, SportsFacility> facilities =  new ConcurrentHashMap<>();

    @Override
    public SportsFacility save(SportsFacility sportsFacility) {
        if(sportsFacility.getId() == null){
            sportsFacility.setId(UUID.randomUUID());
        }
        facilities.put(sportsFacility.getId(), sportsFacility);
        return sportsFacility;
    }

    @Override
    public Optional<SportsFacility> findById(UUID id) {
        return Optional.ofNullable(facilities.get(id));
    }

    @Override
    public List<SportsFacility> findAll() {
        return new ArrayList<>(facilities.values());
    }

    @Override
    public void deleteById(UUID id) {
        facilities.remove(id);
    }
}
