package com.project.uniride;


import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.project.uniride.Entities.CarsEntity;
import com.project.uniride.Entities.RatingsEntity;
import com.project.uniride.Entities.RidesEntity;
import com.project.uniride.Entities.StudentDriverEntity;
import com.project.uniride.Entities.StudentPassengerEntity;
import com.project.uniride.Entities.TipsEntity;
import com.project.uniride.Implementation.RatingType;
import com.project.uniride.Repositories.CarsRepository;
import com.project.uniride.Repositories.RatingsRepository;
import com.project.uniride.Repositories.RidesRepository;
import com.project.uniride.Repositories.StudentDriverRepository;
import com.project.uniride.Repositories.StudentPassengerRepository;
import com.project.uniride.Repositories.TipsRepository;



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
		// Create Student Passengers
		StudentPassengerEntity passenger1 = new StudentPassengerEntity("Hannah", "Smith", "hannah@university.edu", "LSU", "password123");
		StudentPassengerEntity passenger2 = new StudentPassengerEntity("Jermiah", "Doe", "john@university.edu", "LSU", "password456");
		studentPassenger.save(passenger1);
		studentPassenger.save(passenger2);

		// Create Student Drivers
		StudentDriverEntity driver1 = new StudentDriverEntity("Gracie", "Johnson", "mike@university.edu", "LSU", "password789");
		StudentDriverEntity driver2 = new StudentDriverEntity("Jay", "Williams", "sarah@university.edu", "LSU", "password101");
		studentDriver.save(driver1);
		studentDriver.save(driver2);

		// Create Cars
		CarsEntity car1 = new CarsEntity(driver1, "Toyota", "Camry", "Blue", "ABC123", 2020);
		CarsEntity car2 = new CarsEntity(driver2, "Honda", "Civic", "Red", "XYZ789", 2019);
		cars.save(car1);
		cars.save(car2);

		// Create Rides
		RidesEntity ride1 = new RidesEntity(passenger1, driver1, car1, "Dodson Ave", "Highland Rd", BigDecimal.valueOf(5.50), "Complete");
		RidesEntity ride2 = new RidesEntity(passenger2, driver2, car2, "Main St", "College Dr", BigDecimal.valueOf(7.25), "Complete");
		rides.save(ride1);
		rides.save(ride2);

		// Create Tips
		TipsEntity tip1 = new TipsEntity(ride1, driver1, passenger1, BigDecimal.valueOf(2.00));
		TipsEntity tip2 = new TipsEntity(ride2, driver2, passenger2, BigDecimal.valueOf(3.00));
		tips.save(tip1);
		tips.save(tip2);

		// Create Ratings
		RatingsEntity rating1 = new RatingsEntity(driver1, passenger1, 5, RatingType.STUDENT_TO_DRIVER);
		RatingsEntity rating2 = new RatingsEntity(driver2, passenger2, 4, RatingType.DRIVER_TO_STUDENT);
		ratings.save(rating1);
		ratings.save(rating2);

		//print to terminal
		for(StudentPassengerEntity passenger: studentPassenger.findAll()){
			logger.info("lastName{}, School:{}", passenger.getLastName(), passenger.getSchool());
		}

		for(StudentDriverEntity driver: studentDriver.findAll()){
			logger.info("lastName{}, email:{}", driver.getLastName(), driver.getEmail());
		}
		
		for(CarsEntity car: cars.findAll()){
			logger.info("Brand{}, color{}", car.getBrand(), car.getColor());
		}

		for(RidesEntity ride:rides.findAll()){
			logger.info("Pickup Lo{}, Dropoff Lo{}", ride.getPickupLocation(), ride.getDropoffLocation());
		}

		for(TipsEntity tip: tips.findAll()){
			logger.info("tipamount{}", tip.getTipAmount());
		}

		for(RatingsEntity rating:ratings.findAll()){
			logger.info("Stars{}",rating.getStars());
		}
		
	}


	

}
