package com.project.uniride.repository;

import com.project.uniride.model.Schedule;
import com.project.uniride.model.Schedule.ScheduleType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, String> {
    List<Schedule> findByUserIdOrderByDateDesc(String userId);
    List<Schedule> findByDateAndType(LocalDate date, ScheduleType type);
    List<Schedule> findByDayOfWeekAndType(String dayOfWeek, ScheduleType type);
    List<Schedule> findByTypeAndMatchedFalseAndDate(ScheduleType type, LocalDate date);
}
