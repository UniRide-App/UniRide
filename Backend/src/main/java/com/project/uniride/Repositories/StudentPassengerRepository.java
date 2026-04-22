package com.project.uniride.Repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import com.project.uniride.Entities.StudentPassengerEntity;

@RepositoryRestController
public interface StudentPassengerRepository extends CrudRepository<StudentPassengerEntity, Long>{
     StudentPassengerEntity findByEmail(@Param("email") String email);
     List<StudentPassengerEntity> findBySchool(@Param("school") String school);
     StudentPassengerEntity findByVerificationToken(@Param("verificationToken") String token);
    List<StudentPassengerEntity> findByIsVerified(@Param("isVerified") boolean isVerified);
}
