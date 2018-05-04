package ftims.da.ranking.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Trip.
 */
@Entity
@Table(name = "trip")
public class Trip implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "jhi_start", nullable = false)
    private ZonedDateTime start;

    @NotNull
    @Column(name = "duration", nullable = false)
    private String duration;

    @NotNull
    @Column(name = "distance", nullable = false)
    private Long distance;

    @NotNull
    @Column(name = "speeding_distance", nullable = false)
    private Long speedingDistance;

    @NotNull
    @Column(name = "max_speeding_velocity", nullable = false)
    private Integer maxSpeedingVelocity;

    @NotNull
    @Column(name = "sudden_brakings", nullable = false)
    private Integer suddenBrakings;

    @NotNull
    @Column(name = "sudden_accelerations", nullable = false)
    private Integer suddenAccelerations;

    @Column(name = "points")
    private Double points;

    @ManyToOne
    private Driver driver;

    @ManyToMany
    @JoinTable(name = "trip_awards",
               joinColumns = @JoinColumn(name="trips_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="awards_id", referencedColumnName="id"))
    private Set<TripAward> awards = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public Trip start(ZonedDateTime start) {
        this.start = start;
        return this;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public String getDuration() {
        return duration;
    }

    public Trip duration(String duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Long getDistance() {
        return distance;
    }

    public Trip distance(Long distance) {
        this.distance = distance;
        return this;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public Long getSpeedingDistance() {
        return speedingDistance;
    }

    public Trip speedingDistance(Long speedingDistance) {
        this.speedingDistance = speedingDistance;
        return this;
    }

    public void setSpeedingDistance(Long speedingDistance) {
        this.speedingDistance = speedingDistance;
    }

    public Integer getMaxSpeedingVelocity() {
        return maxSpeedingVelocity;
    }

    public Trip maxSpeedingVelocity(Integer maxSpeedingVelocity) {
        this.maxSpeedingVelocity = maxSpeedingVelocity;
        return this;
    }

    public void setMaxSpeedingVelocity(Integer maxSpeedingVelocity) {
        this.maxSpeedingVelocity = maxSpeedingVelocity;
    }

    public Integer getSuddenBrakings() {
        return suddenBrakings;
    }

    public Trip suddenBrakings(Integer suddenBrakings) {
        this.suddenBrakings = suddenBrakings;
        return this;
    }

    public void setSuddenBrakings(Integer suddenBrakings) {
        this.suddenBrakings = suddenBrakings;
    }

    public Integer getSuddenAccelerations() {
        return suddenAccelerations;
    }

    public Trip suddenAccelerations(Integer suddenAccelerations) {
        this.suddenAccelerations = suddenAccelerations;
        return this;
    }

    public void setSuddenAccelerations(Integer suddenAccelerations) {
        this.suddenAccelerations = suddenAccelerations;
    }

    public Double getPoints() {
        return points;
    }

    public Trip points(Double points) {
        this.points = points;
        return this;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public Driver getDriver() {
        return driver;
    }

    public Trip driver(Driver driver) {
        this.driver = driver;
        return this;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Set<TripAward> getAwards() {
        return awards;
    }

    public Trip awards(Set<TripAward> tripAwards) {
        this.awards = tripAwards;
        return this;
    }

    public Trip addAwards(TripAward tripAward) {
        this.awards.add(tripAward);
        return this;
    }

    public Trip removeAwards(TripAward tripAward) {
        this.awards.remove(tripAward);
        return this;
    }

    public void setAwards(Set<TripAward> tripAwards) {
        this.awards = tripAwards;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Trip trip = (Trip) o;
        if (trip.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), trip.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Trip{" +
            "id=" + getId() +
            ", start='" + getStart() + "'" +
            ", duration='" + getDuration() + "'" +
            ", distance=" + getDistance() +
            ", speedingDistance=" + getSpeedingDistance() +
            ", maxSpeedingVelocity=" + getMaxSpeedingVelocity() +
            ", suddenBrakings=" + getSuddenBrakings() +
            ", suddenAccelerations=" + getSuddenAccelerations() +
            ", points=" + getPoints() +
            "}";
    }
}
