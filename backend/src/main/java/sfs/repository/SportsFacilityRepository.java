package sfs.repository;

import sfs.model.SportsFacility;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SportsFacilityRepository {
    SportsFacility save(SportsFacility sportsFacility);
    Optional<SportsFacility> findById(String id);
    List<SportsFacility> findAll();
    void deleteById(String id);
}
