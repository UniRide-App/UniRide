package com.project.uniride.Repositories;

//proves CRUD functionailities to entity classes

import org.springframework.data.repository.CrudRepository;

import com.project.uniride.Entities.CarsEntity;

//Author: Hannah Lowery

public interface CarsRepository extends CrudRepository<CarsEntity, Long> {
    
}
