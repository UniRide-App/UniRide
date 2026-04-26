package com.project.uniride.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.project.uniride.Entities.StudentDriverEntity;
import com.project.uniride.Entities.StudentPassengerEntity;
import com.project.uniride.Entities.TipsEntity;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import java.util.List;
@RepositoryRestController
public interface TipsRepository extends CrudRepository<TipsEntity, Long>{
   List<TipsEntity> findByStudentDriver(@Param("studentDriver") StudentDriverEntity studentDriver);
    List<TipsEntity> findByStudentPassenger(@Param("studentPassenger") StudentPassengerEntity studentPassenger); 
}
