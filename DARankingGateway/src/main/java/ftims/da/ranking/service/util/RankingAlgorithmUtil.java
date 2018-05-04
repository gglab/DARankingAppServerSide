package ftims.da.ranking.service.util;

public class RankingAlgorithmUtil {

    public double getTripPoints(final long distance, final long speedingDistance, final int maxSpeeding, final int noSuddenBrakings, final int noSuddenAcc){
        double result = (distance/1000d) - getPenaltyValue(distance, speedingDistance, maxSpeeding, noSuddenBrakings, noSuddenAcc);
        return result;
    }

    public double getPenaltyValue(final long distance, final long speedingDistance, final int maxSpeeding, final int noSuddenBrakings, final int noSuddenAcc){
        final double distanceM = distance/1000d;
        final double speedingDistanceM = speedingDistance/1000d;
        double result = Math.pow(
            (maxSpeeding*speedingDistanceM/distanceM)
                + 5*(noSuddenAcc/distanceM)
                + 10*(noSuddenBrakings/distanceM),
            1.02);
        return result;
    }
}
