package service;

import model.*;
import org.springframework.stereotype.Service;
import repository.RentalRepository;
import repository.SportsFacilityRepository;
import rest.dto.CreateFacilityRequest;
import rest.dto.UpdateFacilityRequest;

import java.util.List;
import java.util.UUID;

@Service
public class SportsFacilityServiceImpl implements SportsFacilityService{

    private final SportsFacilityRepository sportsFacilityRepository;
    private final RentalRepository rentalRepository;

    public SportsFacilityServiceImpl(SportsFacilityRepository sportsFacilityRepository, RentalRepository rentalRepository){
        this.sportsFacilityRepository = sportsFacilityRepository;
        this.rentalRepository = rentalRepository;
    }

    @Override
    public SportsFacility createFacilityFromDTO(CreateFacilityRequest request) throws Exception {
        SportsFacility sportsFacility;
        String type = request.getFacilityType();
        if(type.equalsIgnoreCase("GYM"))
        {
            sportsFacility = new Gym(
                    request.getName(),
                    request.getPricePerHour(),
                    request.getCapacity(),
                    request.getAreaInSqm(),
                    request.getHasSauna()
            );
        }
        else if(type.equalsIgnoreCase("TENNIS_COURT"))
        {
            SurfaceType typeEnum;
            try {
                typeEnum = SurfaceType.valueOf(request.getSurfaceType().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new Exception("Niepoprawny typ nawierzchni: " + request.getSurfaceType());
            }

            sportsFacility = new TennisCourt(
                    request.getName(),
                    request.getPricePerHour(),
                    request.getCapacity(),
                    typeEnum,
                    request.getIndoor()
            );
        }
        else if (type.equalsIgnoreCase("SWIMMING_POOL"))
        {
            sportsFacility = new SwimmingPool(
                    request.getName(),
                    request.getPricePerHour(),
                    request.getCapacity(),
                    request.getPoolLength(),
                    request.getNumberOfLanes()
            );
        }
        else {
            throw new Exception("Obiekt sportowy nieznanego typu. Nie udało się go stworzyć.");
        }
        return sportsFacilityRepository.save(sportsFacility);
    }

    @Override
    public SportsFacility updateFacility(UUID facilityId, UpdateFacilityRequest request) throws Exception {
        SportsFacility sportsFacility = getFacilityById(facilityId);
        sportsFacility.setName(request.getName());
        sportsFacility.setPricePerHour(request.getPricePerHour());
        sportsFacility.setCapacity(request.getCapacity());
        return sportsFacilityRepository.save(sportsFacility);
    }

    @Override
    public SportsFacility getFacilityById(UUID facilityId) throws Exception {
        return sportsFacilityRepository.findById(facilityId).orElseThrow(() -> new Exception("Nie udało się znaleźć obiektu o ID: " + facilityId + "."));
    }

    @Override
    public List<SportsFacility> getAllFacilities() {
        return sportsFacilityRepository.findAll();
    }

    @Override
    public void deleteFacility(UUID facilityId) throws Exception {
        if (sportsFacilityRepository.findById(facilityId).isEmpty()){
            throw new Exception("Obiekt sportowy o ID: " + facilityId + " nie istnieje, więc nie można go usunąć.");
        }
        if (!rentalRepository.findByFacilityId(facilityId).isEmpty()){
           throw new Exception("Nie można usunąć obiektu o ID: " + facilityId + " , ponieważ jest ZAREZERWOWANY.");
        }
        sportsFacilityRepository.deleteById(facilityId);
    }
}
