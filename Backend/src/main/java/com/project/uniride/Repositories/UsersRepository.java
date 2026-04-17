package com.project.uniride.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.project.uniride.Entities.UsersEntity;

public interface UsersRepository extends CrudRepository<UsersEntity, Long>{
    
}
