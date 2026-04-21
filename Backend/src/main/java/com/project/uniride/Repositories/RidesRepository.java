package com.project.uniride.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.project.uniride.Entities.RidesEntity;
import com.project.uniride.Entities.StudentDriverEntity;
import com.project.uniride.Entities.StudentPassengerEntity;
import java.util.List;
import org.springframework.data.rest.webmvc.RepositoryRestController;

@RepositoryRestController
public interface RidesRepository extends CrudRepository<RidesEntity, Long> {
   List<RidesEntity> findByStatus(@Param("status") String status);
    List<RidesEntity> findByStudentPassenger(@Param("studentPassenger") StudentPassengerEntity studentPassenger);
    List<RidesEntity> findByStudentDriver(@Param("studentDriver") StudentDriverEntity studentDriver);
}
