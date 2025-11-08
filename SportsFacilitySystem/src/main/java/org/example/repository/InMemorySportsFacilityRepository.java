package org.example.repository;

import org.example.model.SportsFacility;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemorySportsFacilityRepository implements SportsFacilityRepository{
    private HashMap<UUID, SportsFacility> facilities =  new HashMap<>();

    @Override
    public SportsFacility save(SportsFacility sportsFacility) {
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
