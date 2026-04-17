package com.project.uniride;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.project.uniride.Entities.CarsEntity;
import com.project.uniride.Entities.RidesEntity;
import com.project.uniride.Entities.UsersEntity;
import com.project.uniride.Repositories.CarsRepository;
import com.project.uniride.Repositories.RidesRepository;
import com.project.uniride.Repositories.UsersRepository;
/**UniRide is a ride share app where Students that goes to the same college are able to share rides with each other. 
 */
@SpringBootApplication
public class UnirideApplication implements CommandLineRunner {
//this is just for testing
	private static final Logger logger= 
			LoggerFactory.getLogger(
				UnirideApplication.class
			);

			private final CarsRepository carsRepository;
			private final RidesRepository ridesRepository;
			private final UsersRepository usersRepository;

			public UnirideApplication(CarsRepository carsRepository, RidesRepository ridesRepository, UsersRepository usersRepository){
				this.carsRepository=carsRepository;
				this.ridesRepository=ridesRepository;
				this.usersRepository=usersRepository;
			}

//only keep this method when not testing
	public static void main(String[] args) {
		SpringApplication.run(UnirideApplication.class, args);
	}

//this is for testing
	@Override
	public void run(String... args) throws Exception{
		carsRepository.save(new CarsEntity(1,"Ford","Mustand","blue","Fr4563",2004));
		ridesRepository.save(new RidesEntity(1,1,2,"LSU","home",20.30,"complete",5.00,5,3));
		usersRepository.save(new UsersEntity("Rebecca","Hurse","RHurse@lsu.edu",985431782,"Driver"));

		//fetch
		for(CarsEntity carsEntity: carsRepository.findAll()){
			logger.info("DriverID:{}, model:{}",
				carsEntity.getDriverID(), carsEntity.getModel());
		}
		for(RidesEntity ridesEntity: ridesRepository.findAll()){
			logger.info("StudentID:{}, model:{}",
				ridesEntity.getStudentID(), ridesEntity.getPickupLocation());
		}
		for(UsersEntity usersEntity: usersRepository.findAll()){
			logger.info("Firstname:{}, email:{}",
				usersEntity.getFirstName(), usersEntity.getEmail());
		}
		
	}

}
