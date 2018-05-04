package ftims.da.ranking.web.rest;

import com.codahale.metrics.annotation.Timed;
import ftims.da.ranking.DaGisMsApp;
import ftims.da.ranking.domain.GisSpeedingLimit;

import ftims.da.ranking.repository.GisSpeedingLimitRepository;
import ftims.da.ranking.web.rest.errors.BadRequestAlertException;
import ftims.da.ranking.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.hibernate.Session;
import org.postgresql.core.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.jca.cci.connection.ConnectionFactoryUtils;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing GisSpeedingLimit.
 */

@RestController
@RequestMapping("/api")
@Transactional
public class GisSpeedingLimitResource {

    private final Logger log = LoggerFactory.getLogger(GisSpeedingLimitResource.class);

    private static final String ENTITY_NAME = "gisSpeedingLimit";

    private final GisSpeedingLimitRepository gisSpeedingLimitRepository;

    public GisSpeedingLimitResource(GisSpeedingLimitRepository gisSpeedingLimitRepository) {
        this.gisSpeedingLimitRepository = gisSpeedingLimitRepository;
    }

//    /**
//     * POST  /gis-speeding-limits : Create a new gisSpeedingLimit.
//     *
//     * @param gisSpeedingLimit the gisSpeedingLimit to create
//     * @return the ResponseEntity with status 201 (Created) and with body the new gisSpeedingLimit, or with status 400 (Bad Request) if the gisSpeedingLimit has already an ID
//     * @throws URISyntaxException if the Location URI syntax is incorrect
//     */
//    @PostMapping("/gis-speeding-limits")
//    @Timed
//    public ResponseEntity<GisSpeedingLimit> createGisSpeedingLimit(@RequestBody GisSpeedingLimit gisSpeedingLimit) throws URISyntaxException {
//        log.debug("REST request to save GisSpeedingLimit : {}", gisSpeedingLimit);
//        if (gisSpeedingLimit.getId() != null) {
//            throw new BadRequestAlertException("A new gisSpeedingLimit cannot already have an ID", ENTITY_NAME, "idexists");
//        }
//        GisSpeedingLimit result = gisSpeedingLimitRepository.save(gisSpeedingLimit);
//        return ResponseEntity.created(new URI("/api/gis-speeding-limits/" + result.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
//            .body(result);
//    }
//
//    /**
//     * PUT  /gis-speeding-limits : Updates an existing gisSpeedingLimit.
//     *
//     * @param gisSpeedingLimit the gisSpeedingLimit to update
//     * @return the ResponseEntity with status 200 (OK) and with body the updated gisSpeedingLimit,
//     * or with status 400 (Bad Request) if the gisSpeedingLimit is not valid,
//     * or with status 500 (Internal Server Error) if the gisSpeedingLimit couldn't be updated
//     * @throws URISyntaxException if the Location URI syntax is incorrect
//     */
//    @PutMapping("/gis-speeding-limits")
//    @Timed
//    public ResponseEntity<GisSpeedingLimit> updateGisSpeedingLimit(@RequestBody GisSpeedingLimit gisSpeedingLimit) throws URISyntaxException {
//        log.debug("REST request to update GisSpeedingLimit : {}", gisSpeedingLimit);
//        if (gisSpeedingLimit.getId() == null) {
//            return createGisSpeedingLimit(gisSpeedingLimit);
//        }
//        GisSpeedingLimit result = gisSpeedingLimitRepository.save(gisSpeedingLimit);
//        return ResponseEntity.ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gisSpeedingLimit.getId().toString()))
//            .body(result);
//    }
//
//    /**
//     * GET  /gis-speeding-limits : get all the gisSpeedingLimits.
//     *
//     * @return the ResponseEntity with status 200 (OK) and the list of gisSpeedingLimits in body
//     */
//    @GetMapping("/gis-speeding-limits")
//    @Timed
//    public List<GisSpeedingLimit> getAllGisSpeedingLimits() {
//        log.debug("REST request to get all GisSpeedingLimits");
//        return gisSpeedingLimitRepository.findAll();
//        }
//
//    /**
//     * GET  /gis-speeding-limits/:id : get the "id" gisSpeedingLimit.
//     *
//     * @param id the id of the gisSpeedingLimit to retrieve
//     * @return the ResponseEntity with status 200 (OK) and with body the gisSpeedingLimit, or with status 404 (Not Found)
//     */
//    @GetMapping("/gis-speeding-limits/{id}")
//    @Timed
//    public ResponseEntity<GisSpeedingLimit> getGisSpeedingLimit(@PathVariable Long id) {
//        log.debug("REST request to get GisSpeedingLimit : {}", id);
//        GisSpeedingLimit gisSpeedingLimit = gisSpeedingLimitRepository.findOne(id);
//        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gisSpeedingLimit));
//    }
//
//    /**
//     * DELETE  /gis-speeding-limits/:id : delete the "id" gisSpeedingLimit.
//     *
//     * @param id the id of the gisSpeedingLimit to delete
//     * @return the ResponseEntity with status 200 (OK)
//     */
//    @DeleteMapping("/gis-speeding-limits/{id}")
//    @Timed
//    public ResponseEntity<Void> deleteGisSpeedingLimit(@PathVariable Long id) {
//        log.debug("REST request to delete GisSpeedingLimit : {}", id);
//        gisSpeedingLimitRepository.delete(id);
//        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
//    }

    /**
     * GET  /gisSpeedingLimit/:latitude/:longitude : get speeding limit for given location
     *
     * @param latitude the latitude double
     * @param longitude the latitude double
     *
     * @return the ResponseEntity with status 200 (OK) and the speeding limit for given location
     * */
    @GetMapping("/gisSpeedingLimit/{latitude}/{longitude:.+}")
    @Timed
    public String getGisSpeedingLimit(@PathVariable Double latitude, @PathVariable Double longitude) {
        log.debug("REST request to get Speeding Limit from GIS for latitude: " + latitude + ", longitude: " + longitude);
        String speedlimit = gisSpeedingLimitRepository.getmaxspeed(latitude, longitude);
        return speedlimit;
    }
}

