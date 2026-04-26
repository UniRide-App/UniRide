package com.project.uniride.Repositories;

//proves CRUD functionailities to entity classes

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import com.project.uniride.Entities.CarsEntity;

//Author: Hannah Lowery
@RepositoryRestController
public interface CarsRepository extends CrudRepository<CarsEntity, Long> {
    List <CarsEntity> findByBrand(@Param("brand") String brand);
    List <CarsEntity>findByColor(@Param("color") String color);
}
