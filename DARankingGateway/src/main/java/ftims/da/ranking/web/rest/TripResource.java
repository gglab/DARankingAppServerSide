package ftims.da.ranking.web.rest;

import com.codahale.metrics.annotation.Timed;
import ftims.da.ranking.domain.Driver;
import ftims.da.ranking.domain.Trip;

import ftims.da.ranking.domain.TripAward;
import ftims.da.ranking.repository.DriverAwardRepository;
import ftims.da.ranking.repository.DriverRepository;
import ftims.da.ranking.repository.TripAwardRepository;
import ftims.da.ranking.repository.TripRepository;
import ftims.da.ranking.security.AuthoritiesConstants;
import ftims.da.ranking.security.SecurityUtils;
import ftims.da.ranking.service.util.DriverAwardsUtil;
import ftims.da.ranking.service.util.RankingAlgorithmUtil;
import ftims.da.ranking.service.util.TripAwardsUtil;
import ftims.da.ranking.web.rest.errors.BadRequestAlertException;
import ftims.da.ranking.web.rest.util.HeaderUtil;
import ftims.da.ranking.web.rest.vm.TripVM;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing Trip.
 */
@RestController
@RequestMapping("/api")
public class TripResource {

    private final Logger log = LoggerFactory.getLogger(TripResource.class);

    private static final String ENTITY_NAME = "trip";

    private final TripRepository tripRepository;
    private final DriverRepository driverRepository;
    private final TripAwardRepository tripAwardsRepository;
    private final DriverAwardRepository driverAwardRepository;
    private final RankingAlgorithmUtil ranking;
    private final TripAwardsUtil tripAwardsUtil;
    private final DriverAwardsUtil driverAwardsUtil;

    public TripResource(TripRepository tripRepository,
                        DriverRepository driverRepository,
                        TripAwardRepository tripAwardsRepository,
                        DriverAwardRepository driverAwardRepository) {
        this.tripRepository = tripRepository;
        this.driverRepository = driverRepository;
        this.tripAwardsRepository = tripAwardsRepository;
        this.driverAwardRepository = driverAwardRepository;
        this.ranking = new RankingAlgorithmUtil();
        this.driverAwardsUtil = new DriverAwardsUtil(driverAwardRepository.findAll());
        this.tripAwardsUtil = new TripAwardsUtil(tripAwardsRepository.findAll());
    }

    /**
     * POST  /trips : Create a new trip.
     *
     * @param trip the trip to create
     * @return the ResponseEntity with status 201 (Created) and with body the new trip, or with status 400 (Bad Request) if the trip has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/trips")
    @Timed
    public ResponseEntity<Trip> createTrip(@Valid @RequestBody Trip trip) throws URISyntaxException {
        log.debug("REST request to save Trip : {}", trip);
        if (trip.getId() != null) {
            throw new BadRequestAlertException("A new trip cannot already have an ID", ENTITY_NAME, "idexists");
        }
        double rank = ranking.getTripPoints(trip.getDistance(),
            trip.getSpeedingDistance(),
            trip.getMaxSpeedingVelocity(),
            trip.getSuddenBrakings(),
            trip.getSuddenAccelerations());
        Set<TripAward> tripAwards = tripAwardsUtil.getTripAwards(trip);
        trip.setAwards(tripAwards);
        for(TripAward award: tripAwards) {
            rank += award.getPoints();
        }
        trip.setPoints(rank);
        Trip result = tripRepository.save(trip);

        Driver driver = driverRepository.findOneWithEagerRelationships(trip.getDriver().getId());
        if(driver.getRank()!=null){
            driver.setRank(driver.getRank() + rank);
        }else{
            driver.setRank(rank);
        }
        List<Trip> driversTrips = tripRepository.findAllWithEagerRelationships();
        driversTrips.removeIf(t -> !t.getDriver().getId().equals(driver.getId()));
        driverAwardsUtil.setDriverAwardsAndRank(driver, driversTrips);
        driverRepository.save(driver);
        return ResponseEntity.created(new URI("/api/trips/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /trips : Updates an existing trip.
     *
     * @param trip the trip to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated trip,
     * or with status 400 (Bad Request) if the trip is not valid,
     * or with status 500 (Internal Server Error) if the trip couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/trips")
    @Timed
    public ResponseEntity<Trip> updateTrip(@Valid @RequestBody Trip trip) throws URISyntaxException {
        log.debug("REST request to update Trip : {}", trip);
        if (trip.getId() == null) {
            return createTrip(trip);
        }
        double rank = ranking.getTripPoints(trip.getDistance(),
            trip.getSpeedingDistance(),
            trip.getMaxSpeedingVelocity(),
            trip.getSuddenBrakings(),
            trip.getSuddenAccelerations());
        Set<TripAward> tripAwards = tripAwardsUtil.getTripAwards(trip);
        trip.setAwards(tripAwards);
        for(TripAward award: tripAwards) {
            rank += award.getPoints();
        }
        trip.setPoints(rank);
        Trip old = tripRepository.findOneWithEagerRelationships(trip.getId());
        Trip result = tripRepository.save(trip);

        Driver driver = driverRepository.findOneWithEagerRelationships(trip.getDriver().getId());

        double oldTripPoints = 0.0;
        double driverPoint = 0.0;
        if(old.getPoints()!=null){
            oldTripPoints = old.getPoints();
        }
        if(driver.getRank()!=null){
            driverPoint = driver.getRank();
        }
        driver.setRank(driverPoint + rank - oldTripPoints);

        List<Trip> driversTrips = tripRepository.findAllWithEagerRelationships();
        driversTrips.removeIf(t -> !t.getDriver().getId().equals(driver.getId()));
        driverAwardsUtil.setDriverAwardsAndRank(driver, driversTrips);

        driverRepository.save(driver);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, trip.getId().toString()))
            .body(result);
    }

    /**
     * GET  /trips : get all the trips.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of trips in body
     */
    @GetMapping("/trips")
    @Timed
    public List<Trip> getAllTrips() {
        log.debug("REST request to get all Trips");
        List<Trip> result = tripRepository.findAllWithEagerRelationships();
        if(!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            result.removeIf(trip->!trip.getDriver().getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin().get()));
        }
        return result;
    }

    /**
     * GET  /trips/:id : get the "id" trip.
     *
     * @param id the id of the trip to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the trip, or with status 404 (Not Found)
     */
    @GetMapping("/trips/{id}")
    @Timed
    public ResponseEntity<Trip> getTrip(@PathVariable Long id) {
        log.debug("REST request to get Trip : {}", id);
        Trip trip = tripRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(trip));
    }

    /**
     * DELETE  /trips/:id : delete the "id" trip.
     *
     * @param id the id of the trip to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/trips/{id}")
    @Timed
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        log.debug("REST request to delete Trip : {}", id);
        Driver driver = null;
        Trip trip = tripRepository.findOneWithEagerRelationships(id);
        if (trip != null) {
            driver = driverRepository.findOneWithEagerRelationships(trip.getDriver().getId());
            double tripPoints = 0.0;
            double driverPoint = 0.0;
            if (trip.getPoints() != null) {
                tripPoints = trip.getPoints();
            }
            if (driver.getRank() != null) {
                driverPoint = driver.getRank();
            }
            driver.setRank(driverPoint - tripPoints);
        }
        tripRepository.delete(id);
        if(driver!=null){
            List<Trip> driversTrips = tripRepository.findAllWithEagerRelationships();
            Long did = driver.getId();
            driversTrips.removeIf(t -> !t.getDriver().getId().equals(did));
            driverAwardsUtil.setDriverAwardsAndRank(driver, driversTrips);
            driverRepository.save(driver);
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * POST  /tripAgent : Create a new trip.
     *
     * @param tripVM the tripVM to create
     * @return the ResponseEntity with status 201 (Created) and with body the new trip, or with status 400 (Bad Request) if the trip has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tripAgent")
    @Timed
    public ResponseEntity<Trip> createTripFromAgent(@Valid @RequestBody TripVM tripVM) throws URISyntaxException {
        log.debug("REST request to save TripVM : {}", tripVM);
        Trip trip = new Trip();
        trip.setStart(ZonedDateTime.ofInstant(tripVM.getStart().toInstant(), ZoneId.systemDefault()));
        trip.setDuration(tripVM.getDuration());
        trip.setDistance(tripVM.getDistance());
        trip.setSpeedingDistance(tripVM.getSpeedingDistance());
        trip.setMaxSpeedingVelocity(tripVM.getMaxSpeedingVelocity());
        trip.setSuddenAccelerations(tripVM.getSuddenAccelerations());
        trip.setSuddenBrakings(tripVM.getSuddenBrakings());
        Driver driver = driverRepository.findOneWithEagerRelationships(tripVM.getDriver());
        trip.setDriver(driver);
        double rank = ranking.getTripPoints(trip.getDistance(),
            trip.getSpeedingDistance(),
            trip.getMaxSpeedingVelocity(),
            trip.getSuddenBrakings(),
            trip.getSuddenAccelerations());
        Set<TripAward> tripAwards = tripAwardsUtil.getTripAwards(trip);
        trip.setAwards(tripAwards);
        for(TripAward award: tripAwards) {
            rank += award.getPoints();
        }
        trip.setPoints(rank);
        Trip result = tripRepository.save(trip);
        if(driver.getRank()!=null){
            driver.setRank(driver.getRank() + rank);
        }else{
            driver.setRank(rank);
        }
        List<Trip> driversTrips = tripRepository.findAllWithEagerRelationships();
        driversTrips.removeIf(t -> !t.getDriver().getId().equals(driver.getId()));
        driverAwardsUtil.setDriverAwardsAndRank(driver, driversTrips);
        driverRepository.save(driver);
        return ResponseEntity.created(new URI("/api/tripAgent/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


}
