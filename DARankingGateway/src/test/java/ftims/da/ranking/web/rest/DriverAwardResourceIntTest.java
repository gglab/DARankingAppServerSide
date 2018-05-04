package ftims.da.ranking.web.rest;

import ftims.da.ranking.DaRankingGatewayApp;

import ftims.da.ranking.domain.DriverAward;
import ftims.da.ranking.repository.DriverAwardRepository;
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
 * Test class for the DriverAwardResource REST controller.
 *
 * @see DriverAwardResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DaRankingGatewayApp.class)
public class DriverAwardResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_POINTS = 1D;
    private static final Double UPDATED_POINTS = 2D;

    @Autowired
    private DriverAwardRepository driverAwardRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDriverAwardMockMvc;

    private DriverAward driverAward;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DriverAwardResource driverAwardResource = new DriverAwardResource(driverAwardRepository);
        this.restDriverAwardMockMvc = MockMvcBuilders.standaloneSetup(driverAwardResource)
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
    public static DriverAward createEntity(EntityManager em) {
        DriverAward driverAward = new DriverAward()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .points(DEFAULT_POINTS);
        return driverAward;
    }

    @Before
    public void initTest() {
        driverAward = createEntity(em);
    }

    @Test
    @Transactional
    public void createDriverAward() throws Exception {
        int databaseSizeBeforeCreate = driverAwardRepository.findAll().size();

        // Create the DriverAward
        restDriverAwardMockMvc.perform(post("/api/driver-awards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(driverAward)))
            .andExpect(status().isCreated());

        // Validate the DriverAward in the database
        List<DriverAward> driverAwardList = driverAwardRepository.findAll();
        assertThat(driverAwardList).hasSize(databaseSizeBeforeCreate + 1);
        DriverAward testDriverAward = driverAwardList.get(driverAwardList.size() - 1);
        assertThat(testDriverAward.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDriverAward.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDriverAward.getPoints()).isEqualTo(DEFAULT_POINTS);
    }

    @Test
    @Transactional
    public void createDriverAwardWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = driverAwardRepository.findAll().size();

        // Create the DriverAward with an existing ID
        driverAward.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDriverAwardMockMvc.perform(post("/api/driver-awards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(driverAward)))
            .andExpect(status().isBadRequest());

        // Validate the DriverAward in the database
        List<DriverAward> driverAwardList = driverAwardRepository.findAll();
        assertThat(driverAwardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = driverAwardRepository.findAll().size();
        // set the field null
        driverAward.setName(null);

        // Create the DriverAward, which fails.

        restDriverAwardMockMvc.perform(post("/api/driver-awards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(driverAward)))
            .andExpect(status().isBadRequest());

        List<DriverAward> driverAwardList = driverAwardRepository.findAll();
        assertThat(driverAwardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = driverAwardRepository.findAll().size();
        // set the field null
        driverAward.setPoints(null);

        // Create the DriverAward, which fails.

        restDriverAwardMockMvc.perform(post("/api/driver-awards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(driverAward)))
            .andExpect(status().isBadRequest());

        List<DriverAward> driverAwardList = driverAwardRepository.findAll();
        assertThat(driverAwardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDriverAwards() throws Exception {
        // Initialize the database
        driverAwardRepository.saveAndFlush(driverAward);

        // Get all the driverAwardList
        restDriverAwardMockMvc.perform(get("/api/driver-awards?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(driverAward.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.doubleValue())));
    }

    @Test
    @Transactional
    public void getDriverAward() throws Exception {
        // Initialize the database
        driverAwardRepository.saveAndFlush(driverAward);

        // Get the driverAward
        restDriverAwardMockMvc.perform(get("/api/driver-awards/{id}", driverAward.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(driverAward.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDriverAward() throws Exception {
        // Get the driverAward
        restDriverAwardMockMvc.perform(get("/api/driver-awards/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDriverAward() throws Exception {
        // Initialize the database
        driverAwardRepository.saveAndFlush(driverAward);
        int databaseSizeBeforeUpdate = driverAwardRepository.findAll().size();

        // Update the driverAward
        DriverAward updatedDriverAward = driverAwardRepository.findOne(driverAward.getId());
        // Disconnect from session so that the updates on updatedDriverAward are not directly saved in db
        em.detach(updatedDriverAward);
        updatedDriverAward
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .points(UPDATED_POINTS);

        restDriverAwardMockMvc.perform(put("/api/driver-awards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDriverAward)))
            .andExpect(status().isOk());

        // Validate the DriverAward in the database
        List<DriverAward> driverAwardList = driverAwardRepository.findAll();
        assertThat(driverAwardList).hasSize(databaseSizeBeforeUpdate);
        DriverAward testDriverAward = driverAwardList.get(driverAwardList.size() - 1);
        assertThat(testDriverAward.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDriverAward.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDriverAward.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void updateNonExistingDriverAward() throws Exception {
        int databaseSizeBeforeUpdate = driverAwardRepository.findAll().size();

        // Create the DriverAward

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDriverAwardMockMvc.perform(put("/api/driver-awards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(driverAward)))
            .andExpect(status().isCreated());

        // Validate the DriverAward in the database
        List<DriverAward> driverAwardList = driverAwardRepository.findAll();
        assertThat(driverAwardList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDriverAward() throws Exception {
        // Initialize the database
        driverAwardRepository.saveAndFlush(driverAward);
        int databaseSizeBeforeDelete = driverAwardRepository.findAll().size();

        // Get the driverAward
        restDriverAwardMockMvc.perform(delete("/api/driver-awards/{id}", driverAward.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DriverAward> driverAwardList = driverAwardRepository.findAll();
        assertThat(driverAwardList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DriverAward.class);
        DriverAward driverAward1 = new DriverAward();
        driverAward1.setId(1L);
        DriverAward driverAward2 = new DriverAward();
        driverAward2.setId(driverAward1.getId());
        assertThat(driverAward1).isEqualTo(driverAward2);
        driverAward2.setId(2L);
        assertThat(driverAward1).isNotEqualTo(driverAward2);
        driverAward1.setId(null);
        assertThat(driverAward1).isNotEqualTo(driverAward2);
    }
}
