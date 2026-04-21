package com.project.uniride.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.project.uniride.Entities.RatingsEntity;
import com.project.uniride.Entities.StudentDriverEntity;
import com.project.uniride.Entities.StudentPassengerEntity;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import java.util.List;

@RepositoryRestController
public interface  RatingsRepository extends CrudRepository<RatingsEntity, Long>{
     List<RatingsEntity> findByStudentDriver(@Param("studentDriver") StudentDriverEntity studentDriver);
    List<RatingsEntity> findByStudentPassenger(@Param("studentPassenger") StudentPassengerEntity studentPassenger);
    List<RatingsEntity> findByRatingType(@Param("ratingType") String ratingType);
}
