package com.api.personalspringapi.services;

import com.api.personalspringapi.models.ModelParkingSpot;
import com.api.personalspringapi.respositories.RepositoryParkingSpot;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ServiceParkingSpot {
    final RepositoryParkingSpot repositoryParkingSpot;
    final private ObservationRegistry observationRegistry;

    public ServiceParkingSpot(RepositoryParkingSpot repositoryParkingSpot, ObservationRegistry observationRegistry) {
        this.repositoryParkingSpot = repositoryParkingSpot;
        this.observationRegistry = observationRegistry;
    }

    @Transactional
    public ModelParkingSpot save(ModelParkingSpot modelParkingSpot){
        return repositoryParkingSpot.save(modelParkingSpot);
    }

    public boolean existsByLicensePlateCar(String licensePlateCar){
        return repositoryParkingSpot.existsByLicensePlateCar(licensePlateCar);
    }

    public boolean existsByParkingSpotNumber(String parkingSpotNumber){
        return repositoryParkingSpot.existsByParkingSpotNumber(parkingSpotNumber);
    }

    public boolean existsByApartmentAndBlock(String apartment, String block){
        return repositoryParkingSpot.existsByApartmentAndBlock(apartment, block);
    }

    public List<ModelParkingSpot> findAll(Pageable pageable) {
        return Observation
                .createNotStarted("findAll", observationRegistry)
                .lowCardinalityKeyValue("valor", "88")
                .lowCardinalityKeyValue("oi", "2")
                .highCardinalityKeyValue("oiu", "osososo")
                .observe(this::findAllNoObserver);
    }

    public List<ModelParkingSpot> findAllNoObserver(){
        return repositoryParkingSpot.findAll();
    }

    public Optional<ModelParkingSpot> findById(UUID id){
        return repositoryParkingSpot.findById(id);
    }

    @Transactional
    public void delete(ModelParkingSpot modelParkingSpot) {
        repositoryParkingSpot.delete(modelParkingSpot);
    }
}
