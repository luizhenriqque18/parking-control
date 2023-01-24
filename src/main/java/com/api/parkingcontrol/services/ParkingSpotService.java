package com.api.parkingcontrol.services;

import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ParkingSpotService {
    final ParkingSpotRepository repository;

    public ParkingSpotService(ParkingSpotRepository parkingSpotRepository) {
        this.repository = parkingSpotRepository;
    }

    @Transactional
    public ParkingSpotModel save(ParkingSpotModel parkingSpotModel) {
        return repository.save(parkingSpotModel);
    }

    public boolean existsByLicensePlateCar(String licensePlateCar) {
        return repository.existsByLicensePlateCar(licensePlateCar);
    }

    public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
        return repository.existsByParkingSpotNumber(parkingSpotNumber);
    }

    public boolean existsByApartmentAndBlock(String apartment, String block) {
        return repository.existsByApartmentAndBlock(apartment, block);
    }

    @Transactional
    public void delete(ParkingSpotModel parkingSpotModel) {
        repository.delete(parkingSpotModel);
    }

    public Optional<ParkingSpotModel> findById(UUID id) {
        return repository.findById(id);
    }

    public Page<ParkingSpotModel> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}