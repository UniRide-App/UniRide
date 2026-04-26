package com.project.uniride.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    private String id;

    private String userId;
    private String userName;

    public enum ScheduleType { RIDE_REQUEST, DRIVER_AVAILABILITY }

    @Enumerated(EnumType.STRING)
    private ScheduleType type;

    private LocalDate date;
    private LocalTime arrivalTime;
    private String leavingFrom;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "latitude",  column = @Column(name = "leaving_from_lat")),
        @AttributeOverride(name = "longitude", column = @Column(name = "leaving_from_lng"))
    })
    private GeoLocation leavingFromLocation;

    private String goingTo;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "latitude",  column = @Column(name = "going_to_lat")),
        @AttributeOverride(name = "longitude", column = @Column(name = "going_to_lng"))
    })
    private GeoLocation goingToLocation;

    private String dayOfWeek;
    private boolean recurring;

    private String matchedWithUserId;
    private boolean matched;

    private Instant createdAt;

    @PrePersist
    protected void prePersist() {
        if (id == null) id = UUID.randomUUID().toString();
    }

    // ─── Getters & Setters ───
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public ScheduleType getType() { return type; }
    public void setType(ScheduleType type) { this.type = type; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalTime arrivalTime) { this.arrivalTime = arrivalTime; }
    public String getLeavingFrom() { return leavingFrom; }
    public void setLeavingFrom(String leavingFrom) { this.leavingFrom = leavingFrom; }
    public GeoLocation getLeavingFromLocation() { return leavingFromLocation; }
    public void setLeavingFromLocation(GeoLocation l) { this.leavingFromLocation = l; }
    public String getGoingTo() { return goingTo; }
    public void setGoingTo(String goingTo) { this.goingTo = goingTo; }
    public GeoLocation getGoingToLocation() { return goingToLocation; }
    public void setGoingToLocation(GeoLocation g) { this.goingToLocation = g; }
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public boolean isRecurring() { return recurring; }
    public void setRecurring(boolean recurring) { this.recurring = recurring; }
    public String getMatchedWithUserId() { return matchedWithUserId; }
    public void setMatchedWithUserId(String m) { this.matchedWithUserId = m; }
    public boolean isMatched() { return matched; }
    public void setMatched(boolean matched) { this.matched = matched; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
