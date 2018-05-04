package ftims.da.ranking.web.rest.vm;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class TripVM {

    @NotNull
    private Date start;

    @NotNull
    private String duration;

    @NotNull
    private Long distance;

    @NotNull
    private Long speedingDistance;

    @NotNull
    private Integer maxSpeedingVelocity;

    @NotNull
    private Integer suddenBrakings;

    @NotNull
    private Integer suddenAccelerations;


    @NotNull
    private Long driver;

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public Long getSpeedingDistance() {
        return speedingDistance;
    }

    public void setSpeedingDistance(Long speedingDistance) {
        this.speedingDistance = speedingDistance;
    }

    public Integer getMaxSpeedingVelocity() {
        return maxSpeedingVelocity;
    }

    public void setMaxSpeedingVelocity(Integer maxSpeedingVelocity) {
        this.maxSpeedingVelocity = maxSpeedingVelocity;
    }

    public Integer getSuddenBrakings() {
        return suddenBrakings;
    }

    public void setSuddenBrakings(Integer suddenBrakings) {
        this.suddenBrakings = suddenBrakings;
    }

    public Integer getSuddenAccelerations() {
        return suddenAccelerations;
    }

    public void setSuddenAccelerations(Integer suddenAccelerations) {
        this.suddenAccelerations = suddenAccelerations;
    }

    public Long getDriver() {
        return driver;
    }

    public void setDriver(Long driver) {
        this.driver = driver;
    }

    public TripVM(){

    }


    @Override
    public String toString() {
        return "TripVM{" +
            "start=" + start +
            ", duration=" + duration +
            ", distance=" + distance +
            ", speedingDistance=" + speedingDistance +
            ", maxSpeedingVelocity=" + maxSpeedingVelocity +
            ", suddenBrakings=" + suddenBrakings +
            ", suddenAccelerations=" + suddenAccelerations +
            ", driver='" + driver + '\'' +
            '}';
    }

}
