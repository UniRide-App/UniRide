package com.project.uniride.repository;

import com.project.uniride.model.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    List<Schedule> findByUserIdOrderByDateDesc(String userId);
    List<Schedule> findByDateAndType(LocalDate date, Schedule.ScheduleType type);
    List<Schedule> findByDayOfWeekAndType(String dayOfWeek, Schedule.ScheduleType type);

    @Query("{ 'type': 'DRIVER_AVAILABILITY', 'matched': false, 'date': ?0 }")
    List<Schedule> findAvailableDriversForDate(LocalDate date);

    @Query("{ 'type': 'RIDE_REQUEST', 'matched': false, 'date': ?0 }")
    List<Schedule> findUnmatchedRideRequestsForDate(LocalDate date);
}
