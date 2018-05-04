package ftims.da.ranking.service.util;

import ftims.da.ranking.domain.Driver;
import ftims.da.ranking.domain.DriverAward;
import ftims.da.ranking.domain.Trip;
import org.hibernate.mapping.Collection;

import java.util.*;

import static ftims.da.ranking.service.util.TripAwardsUtil.*;

public class DriverAwardsUtil {
    private static final String WINNER = "Winner";
    private static final String PERFECT_DRIVER = "Perfect Driver";
    private static final String PERFECT100KM = "Perfect Last 100KM";
    private static final String SPEEDING = "Frequent Speeding";
    private static final String NOSPEEDING = "No Speeding Bonus";
    private static final long KM100 = 100000;
    private final List<DriverAward> allDriverAwards;

    public DriverAwardsUtil(List<DriverAward> allDriverAwards){
        this.allDriverAwards = allDriverAwards;

    }

    public Driver setDriverAwardsAndRank(Driver driver, List<Trip> driverTrips){
        Set<DriverAward> newAwards = new HashSet<DriverAward>();
        Double currentRank = driver.getRank();
        Double currentAwardPoints = getDriverAwardsPoints(driver);
        Collections.sort(driverTrips, new Comparator<Trip>() {
            @Override
            public int compare(Trip t1, Trip t2) {
                //from newest to oldest
                return t2.getStart().compareTo(t1.getStart());
            }
        });
        Long distance = 0l;
        Boolean isPerfectLast100 = true;
        Boolean isNoSpeeding = true;
        Boolean isFrequentSpeeding = true;
        Boolean isPerfect = true;
        int i = 0;
        for(Trip trip : driverTrips) {

            i++;
            distance += trip.getDistance();
            if (distance < KM100) {
                isPerfectLast100 &= isPerfectTrip(trip.getSpeedingDistance(), trip.getMaxSpeedingVelocity(), trip.getSuddenBrakings(), trip.getSuddenAccelerations());
            }
            if(i <= 5){
                Boolean speeding = isSpeeding(trip.getDistance(), trip.getSpeedingDistance());
                isFrequentSpeeding &= speeding;
                isNoSpeeding &= !speeding;
            }
            if(i <= 10){
                isPerfect &= isPerfectTrip(trip.getSpeedingDistance(), trip.getMaxSpeedingVelocity(), trip.getSuddenBrakings(), trip.getSuddenAccelerations());
            }
        }
        Double newAwardPoints = 0.0;
        if(distance >= KM100 && isPerfectLast100){
            DriverAward award = allDriverAwards.stream().filter(a->a.getName().equals(PERFECT100KM)).findFirst().get();
            newAwardPoints += award.getPoints();
            newAwards.add(award);
        }
        if(i>=5){
            if(isFrequentSpeeding){
                DriverAward award = allDriverAwards.stream().filter(a->a.getName().equals(SPEEDING)).findFirst().get();
                newAwardPoints += award.getPoints();
                newAwards.add(award);
            }else if(isNoSpeeding){
                DriverAward award = allDriverAwards.stream().filter(a->a.getName().equals(NOSPEEDING)).findFirst().get();
                newAwardPoints += award.getPoints();
                newAwards.add(award);
            }
        }
        if(i>=10 && isPerfect){
            DriverAward award = allDriverAwards.stream().filter(a->a.getName().equals(PERFECT_DRIVER)).findFirst().get();
            newAwardPoints += award.getPoints();
            newAwards.add(award);
        }
        driver.setAwards(newAwards);
        driver.setRank(currentRank - currentAwardPoints + newAwardPoints);
        return driver;
    }

//    public Driver setWinner(Driver driver){
//        DriverAward award = allDriverAwards.stream().filter(a->a.getName().equals(WINNER)).findFirst().get();
//        driver.setRank(driver.getRank() + award.getPoints());
//        driver.addAwards(award);
//        return driver;
//    }

    private Double getDriverAwardsPoints(Driver driver){
        Double result = 0.0;
        for(DriverAward award : driver.getAwards()){
            result += award.getPoints();
        }
        return result;
    }
}
