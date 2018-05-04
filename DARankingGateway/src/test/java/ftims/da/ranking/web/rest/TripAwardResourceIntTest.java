package ftims.da.ranking.web.rest;

import ftims.da.ranking.DaRankingGatewayApp;

import ftims.da.ranking.domain.TripAward;
import ftims.da.ranking.repository.TripAwardRepository;
import ftims.da.ranking.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static ftims.da.ranking.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TripAwardResource REST controller.
 *
 * @see TripAwardResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DaRankingGatewayApp.class)
public class TripAwardResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_POINTS = 1D;
    private static final Double UPDATED_POINTS = 2D;

    @Autowired
    private TripAwardRepository tripAwardRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTripAwardMockMvc;

    private TripAward tripAward;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TripAwardResource tripAwardResource = new TripAwardResource(tripAwardRepository);
        this.restTripAwardMockMvc = MockMvcBuilders.standaloneSetup(tripAwardResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TripAward createEntity(EntityManager em) {
        TripAward tripAward = new TripAward()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .points(DEFAULT_POINTS);
        return tripAward;
    }

    @Before
    public void initTest() {
        tripAward = createEntity(em);
    }

    @Test
    @Transactional
    public void createTripAward() throws Exception {
        int databaseSizeBeforeCreate = tripAwardRepository.findAll().size();

        // Create the TripAward
        restTripAwardMockMvc.perform(post("/api/trip-awards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tripAward)))
            .andExpect(status().isCreated());

        // Validate the TripAward in the database
        List<TripAward> tripAwardList = tripAwardRepository.findAll();
        assertThat(tripAwardList).hasSize(databaseSizeBeforeCreate + 1);
        TripAward testTripAward = tripAwardList.get(tripAwardList.size() - 1);
        assertThat(testTripAward.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTripAward.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTripAward.getPoints()).isEqualTo(DEFAULT_POINTS);
    }

    @Test
    @Transactional
    public void createTripAwardWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tripAwardRepository.findAll().size();

        // Create the TripAward with an existing ID
        tripAward.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTripAwardMockMvc.perform(post("/api/trip-awards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tripAward)))
            .andExpect(status().isBadRequest());

        // Validate the TripAward in the database
        List<TripAward> tripAwardList = tripAwardRepository.findAll();
        assertThat(tripAwardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tripAwardRepository.findAll().size();
        // set the field null
        tripAward.setName(null);

        // Create the TripAward, which fails.

        restTripAwardMockMvc.perform(post("/api/trip-awards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tripAward)))
            .andExpect(status().isBadRequest());

        List<TripAward> tripAwardList = tripAwardRepository.findAll();
        assertThat(tripAwardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = tripAwardRepository.findAll().size();
        // set the field null
        tripAward.setPoints(null);

        // Create the TripAward, which fails.

        restTripAwardMockMvc.perform(post("/api/trip-awards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tripAward)))
            .andExpect(status().isBadRequest());

        List<TripAward> tripAwardList = tripAwardRepository.findAll();
        assertThat(tripAwardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTripAwards() throws Exception {
        // Initialize the database
        tripAwardRepository.saveAndFlush(tripAward);

        // Get all the tripAwardList
        restTripAwardMockMvc.perform(get("/api/trip-awards?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tripAward.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.doubleValue())));
    }

    @Test
    @Transactional
    public void getTripAward() throws Exception {
        // Initialize the database
        tripAwardRepository.saveAndFlush(tripAward);

        // Get the tripAward
        restTripAwardMockMvc.perform(get("/api/trip-awards/{id}", tripAward.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tripAward.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTripAward() throws Exception {
        // Get the tripAward
        restTripAwardMockMvc.perform(get("/api/trip-awards/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTripAward() throws Exception {
        // Initialize the database
        tripAwardRepository.saveAndFlush(tripAward);
        int databaseSizeBeforeUpdate = tripAwardRepository.findAll().size();

        // Update the tripAward
        TripAward updatedTripAward = tripAwardRepository.findOne(tripAward.getId());
        // Disconnect from session so that the updates on updatedTripAward are not directly saved in db
        em.detach(updatedTripAward);
        updatedTripAward
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .points(UPDATED_POINTS);

        restTripAwardMockMvc.perform(put("/api/trip-awards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTripAward)))
            .andExpect(status().isOk());

        // Validate the TripAward in the database
        List<TripAward> tripAwardList = tripAwardRepository.findAll();
        assertThat(tripAwardList).hasSize(databaseSizeBeforeUpdate);
        TripAward testTripAward = tripAwardList.get(tripAwardList.size() - 1);
        assertThat(testTripAward.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTripAward.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTripAward.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void updateNonExistingTripAward() throws Exception {
        int databaseSizeBeforeUpdate = tripAwardRepository.findAll().size();

        // Create the TripAward

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTripAwardMockMvc.perform(put("/api/trip-awards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tripAward)))
            .andExpect(status().isCreated());

        // Validate the TripAward in the database
        List<TripAward> tripAwardList = tripAwardRepository.findAll();
        assertThat(tripAwardList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTripAward() throws Exception {
        // Initialize the database
        tripAwardRepository.saveAndFlush(tripAward);
        int databaseSizeBeforeDelete = tripAwardRepository.findAll().size();

        // Get the tripAward
        restTripAwardMockMvc.perform(delete("/api/trip-awards/{id}", tripAward.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TripAward> tripAwardList = tripAwardRepository.findAll();
        assertThat(tripAwardList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TripAward.class);
        TripAward tripAward1 = new TripAward();
        tripAward1.setId(1L);
        TripAward tripAward2 = new TripAward();
        tripAward2.setId(tripAward1.getId());
        assertThat(tripAward1).isEqualTo(tripAward2);
        tripAward2.setId(2L);
        assertThat(tripAward1).isNotEqualTo(tripAward2);
        tripAward1.setId(null);
        assertThat(tripAward1).isNotEqualTo(tripAward2);
    }
}
