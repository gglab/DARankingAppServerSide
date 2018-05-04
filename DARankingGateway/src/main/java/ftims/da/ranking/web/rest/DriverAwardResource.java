package ftims.da.ranking.web.rest;

import com.codahale.metrics.annotation.Timed;
import ftims.da.ranking.domain.DriverAward;

import ftims.da.ranking.repository.DriverAwardRepository;
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
 * REST controller for managing DriverAward.
 */
@RestController
@RequestMapping("/api")
public class DriverAwardResource {

    private final Logger log = LoggerFactory.getLogger(DriverAwardResource.class);

    private static final String ENTITY_NAME = "driverAward";

    private final DriverAwardRepository driverAwardRepository;

    public DriverAwardResource(DriverAwardRepository driverAwardRepository) {
        this.driverAwardRepository = driverAwardRepository;
    }

    /**
     * POST  /driver-awards : Create a new driverAward.
     *
     * @param driverAward the driverAward to create
     * @return the ResponseEntity with status 201 (Created) and with body the new driverAward, or with status 400 (Bad Request) if the driverAward has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/driver-awards")
    @Timed
    public ResponseEntity<DriverAward> createDriverAward(@Valid @RequestBody DriverAward driverAward) throws URISyntaxException {
        log.debug("REST request to save DriverAward : {}", driverAward);
        if (driverAward.getId() != null) {
            throw new BadRequestAlertException("A new driverAward cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DriverAward result = driverAwardRepository.save(driverAward);
        return ResponseEntity.created(new URI("/api/driver-awards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /driver-awards : Updates an existing driverAward.
     *
     * @param driverAward the driverAward to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated driverAward,
     * or with status 400 (Bad Request) if the driverAward is not valid,
     * or with status 500 (Internal Server Error) if the driverAward couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/driver-awards")
    @Timed
    public ResponseEntity<DriverAward> updateDriverAward(@Valid @RequestBody DriverAward driverAward) throws URISyntaxException {
        log.debug("REST request to update DriverAward : {}", driverAward);
        if (driverAward.getId() == null) {
            return createDriverAward(driverAward);
        }
        DriverAward result = driverAwardRepository.save(driverAward);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, driverAward.getId().toString()))
            .body(result);
    }

    /**
     * GET  /driver-awards : get all the driverAwards.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of driverAwards in body
     */
    @GetMapping("/driver-awards")
    @Timed
    public List<DriverAward> getAllDriverAwards() {
        log.debug("REST request to get all DriverAwards");
        return driverAwardRepository.findAll();
        }

    /**
     * GET  /driver-awards/:id : get the "id" driverAward.
     *
     * @param id the id of the driverAward to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the driverAward, or with status 404 (Not Found)
     */
    @GetMapping("/driver-awards/{id}")
    @Timed
    public ResponseEntity<DriverAward> getDriverAward(@PathVariable Long id) {
        log.debug("REST request to get DriverAward : {}", id);
        DriverAward driverAward = driverAwardRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(driverAward));
    }

    /**
     * DELETE  /driver-awards/:id : delete the "id" driverAward.
     *
     * @param id the id of the driverAward to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/driver-awards/{id}")
    @Timed
    public ResponseEntity<Void> deleteDriverAward(@PathVariable Long id) {
        log.debug("REST request to delete DriverAward : {}", id);
        driverAwardRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
