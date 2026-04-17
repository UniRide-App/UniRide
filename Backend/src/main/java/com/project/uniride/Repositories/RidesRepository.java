package com.project.uniride.Repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.project.uniride.Entities.RidesEntity;

public interface RidesRepository extends CrudRepository<RidesEntity, Long> {
    //list rides by Student id
    List <RidesEntity> findByStudentID(Long studentID);

    //list rides by Driver id
    List <RidesEntity> findByDriverID(Long driverID);
}
