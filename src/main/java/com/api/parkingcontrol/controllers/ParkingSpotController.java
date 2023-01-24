package com.api.parkingcontrol.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.ParkingSpotService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parking-spot")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ParkingSpotController {

    final ParkingSpotService service;
    final MessageSource messageSource;

    public ParkingSpotController(ParkingSpotService service, MessageSource messageSource) {
        this.service = service;
        this.messageSource = messageSource;
    }

    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto dto) {

        if(service.existsByLicensePlateCar(dto.getLicensePlateCar()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body(messageSource.getMessage("api.response.conflict",new Object[]{"License Plate Car"}, null));
        if(service.existsByParkingSpotNumber(dto.getParkingSpotNumber())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(messageSource.getMessage("api.response.conflict",new Object[]{"Parking Spot Number"}, null));
        }
        if(service.existsByApartmentAndBlock(dto.getApartment(), dto.getBlock())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(messageSource.getMessage("api.response.conflict",new Object[]{"Apartment or Block"}, null));
        }

        var parkingSpotModel = new ParkingSpotModel();
        BeanUtils.copyProperties(dto, parkingSpotModel);
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(parkingSpotModel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id,
                                                    @RequestBody @Valid ParkingSpotDto parkingSpotDto){
        Optional<ParkingSpotModel> parkingSpotModelOptional = service.findById(id);
        if (!parkingSpotModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("api.response.not.found", new Object[]{id}, null));
        }
        var parkingSpotModel = new ParkingSpotModel();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        parkingSpotModel.setId(parkingSpotModelOptional.get().getId());
        parkingSpotModel.setRegistrationDate(parkingSpotModelOptional.get().getRegistrationDate());
        return ResponseEntity.status(HttpStatus.OK).body(service.save(parkingSpotModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") UUID id){
        Optional<ParkingSpotModel> parkingSpotModelOptional = service.findById(id);
        if (!parkingSpotModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("api.response.not.found", new Object[]{id}, null));
        }
        service.delete(parkingSpotModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage("api.response.deleted", new Object[]{id}, null));
    }

    @GetMapping
    public ResponseEntity<Page<ParkingSpotModel>> getAllParkingSpots(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneParkingSpot(@PathVariable("id") UUID id) {
        Optional<ParkingSpotModel> parkingSpotModel = service.findById(id);

        if(!parkingSpotModel.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("api.response.not.found", new Object[]{id}, null));

        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModel.get());
    }
}
