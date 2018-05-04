package ftims.da.ranking.web.rest;

import com.codahale.metrics.annotation.Timed;
import ftims.da.ranking.domain.TripAward;

import ftims.da.ranking.repository.TripAwardRepository;
import ftims.da.ranking.web.rest.errors.BadRequestAlertException;
import ftims.da.ranking.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TripAward.
 */
@RestController
@RequestMapping("/api")
public class TripAwardResource {

    private final Logger log = LoggerFactory.getLogger(TripAwardResource.class);

    private static final String ENTITY_NAME = "tripAward";

    private final TripAwardRepository tripAwardRepository;

    public TripAwardResource(TripAwardRepository tripAwardRepository) {
        this.tripAwardRepository = tripAwardRepository;
    }

    /**
     * POST  /trip-awards : Create a new tripAward.
     *
     * @param tripAward the tripAward to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tripAward, or with status 400 (Bad Request) if the tripAward has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/trip-awards")
    @Timed
    public ResponseEntity<TripAward> createTripAward(@Valid @RequestBody TripAward tripAward) throws URISyntaxException {
        log.debug("REST request to save TripAward : {}", tripAward);
        if (tripAward.getId() != null) {
            throw new BadRequestAlertException("A new tripAward cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TripAward result = tripAwardRepository.save(tripAward);
        return ResponseEntity.created(new URI("/api/trip-awards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /trip-awards : Updates an existing tripAward.
     *
     * @param tripAward the tripAward to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tripAward,
     * or with status 400 (Bad Request) if the tripAward is not valid,
     * or with status 500 (Internal Server Error) if the tripAward couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/trip-awards")
    @Timed
    public ResponseEntity<TripAward> updateTripAward(@Valid @RequestBody TripAward tripAward) throws URISyntaxException {
        log.debug("REST request to update TripAward : {}", tripAward);
        if (tripAward.getId() == null) {
            return createTripAward(tripAward);
        }
        TripAward result = tripAwardRepository.save(tripAward);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tripAward.getId().toString()))
            .body(result);
    }

    /**
     * GET  /trip-awards : get all the tripAwards.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tripAwards in body
     */
    @GetMapping("/trip-awards")
    @Timed
    public List<TripAward> getAllTripAwards() {
        log.debug("REST request to get all TripAwards");
        return tripAwardRepository.findAll();
        }

    /**
     * GET  /trip-awards/:id : get the "id" tripAward.
     *
     * @param id the id of the tripAward to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tripAward, or with status 404 (Not Found)
     */
    @GetMapping("/trip-awards/{id}")
    @Timed
    public ResponseEntity<TripAward> getTripAward(@PathVariable Long id) {
        log.debug("REST request to get TripAward : {}", id);
        TripAward tripAward = tripAwardRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tripAward));
    }

    /**
     * DELETE  /trip-awards/:id : delete the "id" tripAward.
     *
     * @param id the id of the tripAward to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/trip-awards/{id}")
    @Timed
    public ResponseEntity<Void> deleteTripAward(@PathVariable Long id) {
        log.debug("REST request to delete TripAward : {}", id);
        tripAwardRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
