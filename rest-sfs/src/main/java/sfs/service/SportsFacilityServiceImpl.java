package sfs.service;

import io.quarkus.security.IdentityAttribute;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sfs.exception.ResourceNotFoundException;
import sfs.model.*;
import sfs.repository.MongoRentalRepository;
import sfs.repository.MongoSportsFacilityRepository;
import sfs.repository.RentalRepository;
import sfs.repository.SportsFacilityRepository;
import sfs.rest.dto.*;

import java.util.List;
import java.util.UUID;


@ApplicationScoped
public class SportsFacilityServiceImpl implements SportsFacilityService{

    private final MongoSportsFacilityRepository sportsFacilityRepository;
    private final MongoRentalRepository rentalRepository;

    @Inject
    public SportsFacilityServiceImpl(MongoSportsFacilityRepository sportsFacilityRepository, MongoRentalRepository rentalRepository){
        this.sportsFacilityRepository = sportsFacilityRepository;
        this.rentalRepository = rentalRepository;
    }

    @Override
    public Gym createGym(CreateGymRequest request) {
        Gym gym = new Gym(
                request.getName(),
                request.getPricePerHour(),
                request.getCapacity(),
                request.getAreaInSqm(),
                request.getHasSauna()
        );
        return (Gym) sportsFacilityRepository.save(gym);
    }

    @Override
    public TennisCourt createTennisCourt(CreateTennisCourtRequest request) throws Exception {
        SurfaceType typeEnum;
        try {
            typeEnum = SurfaceType.valueOf(request.getSurfaceType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new Exception("Niepoprawny typ nawierzchni: " + request.getSurfaceType());
        }

        TennisCourt court = new TennisCourt(
                request.getName(),
                request.getPricePerHour(),
                request.getCapacity(),
                typeEnum,
                request.getIsIndoor()
        );
        return (TennisCourt) sportsFacilityRepository.save(court);
    }

    @Override
    public SwimmingPool createSwimmingPool(CreateSwimmingPoolRequest request) {
        SwimmingPool pool = new SwimmingPool(
                request.getName(),
                request.getPricePerHour(),
                request.getCapacity(),
                request.getPoolLength(),
                request.getNumberOfLanes()
        );
        return (SwimmingPool) sportsFacilityRepository.save(pool);
    }


    @Override
    public Gym updateGym(String id, UpdateGymRequest request) throws Exception {
        SportsFacility facility = getFacilityById(id);
        if (!(facility instanceof Gym)) {
            throw new Exception("Obiekt o ID " + id + " nie jest siłownią.");
        }
        Gym gym = (Gym) facility;
        gym.setName(request.getName());
        gym.setPricePerHour(request.getPricePerHour());
        gym.setCapacity(request.getCapacity());
        gym.setAreaInSqm(request.getAreaInSqm());
        gym.setHasSauna(request.getHasSauna());

        return (Gym) sportsFacilityRepository.save(gym);
    }

    @Override
    public TennisCourt updateTennisCourt(String id, UpdateTennisCourtRequest request) throws Exception {
        SportsFacility facility = getFacilityById(id);
        if (!(facility instanceof TennisCourt)) {
            throw new Exception("Obiekt o ID " + id + " nie jest kortem tenisowym.");
        }
        SurfaceType typeEnum;
        try {
            typeEnum = SurfaceType.valueOf(request.getSurfaceType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new Exception("Niepoprawny typ nawierzchni: " + request.getSurfaceType());
        }

        TennisCourt court = (TennisCourt) facility;
        court.setName(request.getName());
        court.setPricePerHour(request.getPricePerHour());
        court.setCapacity(request.getCapacity());

        court.setSurfaceType(typeEnum);
        court.setIndoor(request.getIsIndoor());

        return (TennisCourt) sportsFacilityRepository.save(court);
    }

    @Override
    public SwimmingPool updateSwimmingPool(String id, UpdateSwimmingPoolRequest request) throws Exception {
        SportsFacility facility = getFacilityById(id);
        if (!(facility instanceof SwimmingPool)) {
            throw new Exception("Obiekt o ID " + id + " nie jest basenem.");
        }
        SwimmingPool pool = (SwimmingPool) facility;
        pool.setName(request.getName());
        pool.setPricePerHour(request.getPricePerHour());
        pool.setCapacity(request.getCapacity());

        pool.setPoolLength(request.getPoolLength());
        pool.setNumberOfLanes(request.getNumberOfLanes());

        return (SwimmingPool) sportsFacilityRepository.save(pool);
    }

    @Override
    public SportsFacility getFacilityById(String facilityId) {
        return sportsFacilityRepository.findById(facilityId).orElseThrow(() -> new ResourceNotFoundException("Nie udało się znaleźć obiektu o ID: " + facilityId + "."));
    }

    @Override
    public List<SportsFacility> getAllFacilities() {
        return sportsFacilityRepository.findAllFacilities();
    }

    @Override
    public void deleteFacility(String facilityId) {
        if (sportsFacilityRepository.findById(facilityId).isEmpty()){
            throw new ResourceNotFoundException("Obiekt sportowy o ID: " + facilityId + " nie istnieje, więc nie można go usunąć.");
        }
        if (!rentalRepository.findByFacilityId(facilityId).isEmpty()){
           throw new ResourceNotFoundException("Nie można usunąć obiektu o ID: " + facilityId + " , ponieważ jest ZAREZERWOWANY.");
        }
        sportsFacilityRepository.deleteById(facilityId);
    }
}
