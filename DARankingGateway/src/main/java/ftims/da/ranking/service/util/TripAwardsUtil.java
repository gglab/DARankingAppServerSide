package ftims.da.ranking.service.util;

import ftims.da.ranking.domain.Trip;
import ftims.da.ranking.domain.TripAward;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TripAwardsUtil {

    private static final String PERFECT_TRIP = "Perfect Trip";
    private static final String GENTLE_TRIP = "Gentle Trip";
    private static final String SPEEDING = "Speeding";

    private List<TripAward> allTripAwards;

    public TripAwardsUtil(List<TripAward> allTripAwards){
        this.allTripAwards = allTripAwards;
    }

    public Set<TripAward> getTripAwards(Trip trip){
        Set<TripAward> result = new HashSet<>();
        if(isPerfectTrip(trip.getSpeedingDistance(), trip.getMaxSpeedingVelocity(), trip.getSuddenBrakings(), trip.getSuddenAccelerations())){
            result.add(allTripAwards.stream().filter(a -> a.getName().equals(PERFECT_TRIP)).findAny().get());
        }
        if(isGentleTrip(trip.getSuddenBrakings(), trip.getSuddenAccelerations())){
            result.add(allTripAwards.stream().filter(a -> a.getName().equals(GENTLE_TRIP)).findAny().get());
        }
        if(isSpeeding(trip.getDistance(), trip.getSpeedingDistance())){
            result.add(allTripAwards.stream().filter(a -> a.getName().equals(SPEEDING)).findAny().get());
        }
        return result;
    }

    protected static boolean isPerfectTrip(Long speedingDist, Integer maxSpeedingV, Integer breaking, Integer acc){
        boolean result = true;
        result &= (speedingDist == null || speedingDist == 0.0);
        result &= (maxSpeedingV == null || maxSpeedingV == 0.0);
        result &= isGentleTrip(breaking, acc);
        return result;
    }
    protected static boolean isGentleTrip(Integer breaking, Integer acc){
        boolean result = true;
        result &= (breaking == null || breaking == 0);
        result &= (acc == null || acc == 0);
        return result;
    }
    protected static boolean isSpeeding(Long distance, Long speeding){
        boolean result = false;
        if(distance != null && speeding != null && distance != 0){
            result = (speeding.doubleValue()/distance.doubleValue() >= 0.5d);
        }
        return result;
    }
}
