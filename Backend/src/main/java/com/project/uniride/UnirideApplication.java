package com.project.uniride;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

import com.project.uniride.Entities.CarsEntity;
import com.project.uniride.Entities.RatingsEntity;
import com.project.uniride.Entities.RidesEntity;
import com.project.uniride.Entities.StudentDriverEntity;
import com.project.uniride.Entities.StudentPassengerEntity;
import com.project.uniride.Entities.TipsEntity;
import com.project.uniride.Repositories.*;



/**UniRide is a ride share app where Students that goes to the same college are able to share rides with each other. 
 */
@SpringBootApplication
public class UnirideApplication implements CommandLineRunner {

	private static final Logger logger =
		LoggerFactory.getLogger( UnirideApplication.class);

	private CarsRepository cars;
	private RatingsRepository ratings;
	private RidesRepository rides;
	private StudentDriverRepository studentDriver;
	private StudentPassengerRepository studentPassenger;
	private TipsRepository tips;


	public UnirideApplication(CarsRepository cars, RatingsRepository ratings, RidesRepository rides,StudentDriverRepository studentDriver, StudentPassengerRepository studentPassenger, TipsRepository tips){
		this.cars=cars;
		this.ratings=ratings;
		this.rides=rides;
		this.studentDriver=studentDriver;
		this.studentPassenger=studentPassenger;
		this.tips=tips;
	}

//only keep this method when not testing
	public static void main(String[] args) {
		SpringApplication.run(UnirideApplication.class, args);
	}

	@Override
	public void run(String...args)throws Exception{
		cars.save(new CarsEntity());
		cars.save(new CarsEntity());
		ratings.save(new RatingsEntity());
		ratings.save(new RatingsEntity());
		rides.save(new RidesEntity());
		rides.save(new RidesEntity());
		studentDriver.save(new StudentDriverEntity());
		studentDriver.save(new StudentDriverEntity());
		studentPassenger.save(new StudentPassengerEntity());
		studentPassenger.save(new StudentPassengerEntity());
		tips.save(new TipsEntity());
		tips.save(new TipsEntity());
		
	}


	

}
