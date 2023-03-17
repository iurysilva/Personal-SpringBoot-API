package com.api.personalspringapi.controllers;

import com.api.personalspringapi.dtos.DtoParkingSpot;
import com.api.personalspringapi.models.ModelParkingSpot;
import com.api.personalspringapi.services.ServiceParkingSpot;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ControllerParkingSpot {
    final ServiceParkingSpot serviceParkingSpot;

    public ControllerParkingSpot(ServiceParkingSpot serviceParkingSpot) {
        this.serviceParkingSpot = serviceParkingSpot;
    }

    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid DtoParkingSpot parkingSpotDto){
        if(serviceParkingSpot.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is already in use!");
        }
        if(serviceParkingSpot.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use!");
        }
        if(serviceParkingSpot.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot already registered for this apartment/block!");
        }
        var modelParkingSpot = new ModelParkingSpot();
        BeanUtils.copyProperties(parkingSpotDto, modelParkingSpot);
        modelParkingSpot.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceParkingSpot.save(modelParkingSpot));
    }

    @GetMapping
    public ResponseEntity<Page<ModelParkingSpot>> getAllParkingSpots(@PageableDefault(page =0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(serviceParkingSpot.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "id") UUID id){
        Optional<ModelParkingSpot> optionalModelParkingSpot = serviceParkingSpot.findById(id);
        if (!optionalModelParkingSpot.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(optionalModelParkingSpot.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") UUID id){
        Optional<ModelParkingSpot> optionalModelParkingSpot = serviceParkingSpot.findById(id);
        if (!optionalModelParkingSpot.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        serviceParkingSpot.delete(optionalModelParkingSpot.get());
        return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deleted successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id,
                                                    @RequestBody @Valid DtoParkingSpot dtoParkingSpot){
        Optional<ModelParkingSpot> optionalModelParkingSpot = serviceParkingSpot.findById(id);
        if (!optionalModelParkingSpot.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        var modelParkingSpot = new ModelParkingSpot();
        BeanUtils.copyProperties(dtoParkingSpot, modelParkingSpot);
        modelParkingSpot.setId(optionalModelParkingSpot.get().getId());
        modelParkingSpot.setRegistrationDate(optionalModelParkingSpot.get().getRegistrationDate());
        return ResponseEntity.status(HttpStatus.OK).body(serviceParkingSpot.save(modelParkingSpot));
    }
}
