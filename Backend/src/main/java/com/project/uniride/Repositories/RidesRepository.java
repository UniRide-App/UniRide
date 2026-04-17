package com.project.uniride.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.project.uniride.Entities.RidesEntity;

public interface RidesRepository extends CrudRepository<RidesEntity, Long> {
    
}
