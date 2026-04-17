package com.project.uniride.Repositories;

//proves CRUD functionailities to entity classes

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.project.uniride.Entities.CarsEntity;

//Author: Hannah Lowery

public interface CarsRepository extends CrudRepository<CarsEntity, Long> {
    //Find car by lisence plate or driverID
    List <CarsEntity> findByDriverIDOrLisencePlate(Long driverID, String lisensePlate);

    //Find car by Brand, Model, or Color
    List <CarsEntity> findByBrandModelOrColor(String brand, String model, String color);
}
