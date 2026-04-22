package com.project.uniride.Repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import com.project.uniride.Entities.StudentDriverEntity;

@RepositoryRestController
public interface StudentDriverRepository extends CrudRepository<StudentDriverEntity, Long>{
     StudentDriverEntity findByEmail(@Param("email") String email);
    List<StudentDriverEntity> findBySchool(@Param("school") String school);
    StudentDriverEntity findByVerificationToken(@Param("verificationToken") String token);
    List<StudentDriverEntity> findByIsVerified(@Param("isVerified") boolean isVerified);
}
