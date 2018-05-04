package ftims.da.ranking.web.rest;

import ftims.da.ranking.DaGisMsApp;

import ftims.da.ranking.domain.GisSpeedingLimit;
import ftims.da.ranking.repository.GisSpeedingLimitRepository;
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
 * Test class for the GisSpeedingLimitResource REST controller.
 *
 * @see GisSpeedingLimitResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DaGisMsApp.class)
public class GisSpeedingLimitResourceIntTest {

    @Autowired
    private GisSpeedingLimitRepository gisSpeedingLimitRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGisSpeedingLimitMockMvc;

    private GisSpeedingLimit gisSpeedingLimit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GisSpeedingLimitResource gisSpeedingLimitResource = new GisSpeedingLimitResource(gisSpeedingLimitRepository);
        this.restGisSpeedingLimitMockMvc = MockMvcBuilders.standaloneSetup(gisSpeedingLimitResource)
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
    public static GisSpeedingLimit createEntity(EntityManager em) {
        GisSpeedingLimit gisSpeedingLimit = new GisSpeedingLimit();
        return gisSpeedingLimit;
    }

    @Before
    public void initTest() {
        gisSpeedingLimit = createEntity(em);
    }

    @Test
    @Transactional
    public void createGisSpeedingLimit() throws Exception {
        int databaseSizeBeforeCreate = gisSpeedingLimitRepository.findAll().size();

        // Create the GisSpeedingLimit
        restGisSpeedingLimitMockMvc.perform(post("/api/gis-speeding-limits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gisSpeedingLimit)))
            .andExpect(status().isCreated());

        // Validate the GisSpeedingLimit in the database
        List<GisSpeedingLimit> gisSpeedingLimitList = gisSpeedingLimitRepository.findAll();
        assertThat(gisSpeedingLimitList).hasSize(databaseSizeBeforeCreate + 1);
        GisSpeedingLimit testGisSpeedingLimit = gisSpeedingLimitList.get(gisSpeedingLimitList.size() - 1);
    }

    @Test
    @Transactional
    public void createGisSpeedingLimitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gisSpeedingLimitRepository.findAll().size();

        // Create the GisSpeedingLimit with an existing ID
        gisSpeedingLimit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGisSpeedingLimitMockMvc.perform(post("/api/gis-speeding-limits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gisSpeedingLimit)))
            .andExpect(status().isBadRequest());

        // Validate the GisSpeedingLimit in the database
        List<GisSpeedingLimit> gisSpeedingLimitList = gisSpeedingLimitRepository.findAll();
        assertThat(gisSpeedingLimitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllGisSpeedingLimits() throws Exception {
        // Initialize the database
        gisSpeedingLimitRepository.saveAndFlush(gisSpeedingLimit);

        // Get all the gisSpeedingLimitList
        restGisSpeedingLimitMockMvc.perform(get("/api/gis-speeding-limits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gisSpeedingLimit.getId().intValue())));
    }

    @Test
    @Transactional
    public void getGisSpeedingLimit() throws Exception {
        // Initialize the database
        gisSpeedingLimitRepository.saveAndFlush(gisSpeedingLimit);

        // Get the gisSpeedingLimit
        restGisSpeedingLimitMockMvc.perform(get("/api/gis-speeding-limits/{id}", gisSpeedingLimit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(gisSpeedingLimit.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingGisSpeedingLimit() throws Exception {
        // Get the gisSpeedingLimit
        restGisSpeedingLimitMockMvc.perform(get("/api/gis-speeding-limits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGisSpeedingLimit() throws Exception {
        // Initialize the database
        gisSpeedingLimitRepository.saveAndFlush(gisSpeedingLimit);
        int databaseSizeBeforeUpdate = gisSpeedingLimitRepository.findAll().size();

        // Update the gisSpeedingLimit
        GisSpeedingLimit updatedGisSpeedingLimit = gisSpeedingLimitRepository.findOne(gisSpeedingLimit.getId());
        // Disconnect from session so that the updates on updatedGisSpeedingLimit are not directly saved in db
        em.detach(updatedGisSpeedingLimit);

        restGisSpeedingLimitMockMvc.perform(put("/api/gis-speeding-limits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGisSpeedingLimit)))
            .andExpect(status().isOk());

        // Validate the GisSpeedingLimit in the database
        List<GisSpeedingLimit> gisSpeedingLimitList = gisSpeedingLimitRepository.findAll();
        assertThat(gisSpeedingLimitList).hasSize(databaseSizeBeforeUpdate);
        GisSpeedingLimit testGisSpeedingLimit = gisSpeedingLimitList.get(gisSpeedingLimitList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingGisSpeedingLimit() throws Exception {
        int databaseSizeBeforeUpdate = gisSpeedingLimitRepository.findAll().size();

        // Create the GisSpeedingLimit

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGisSpeedingLimitMockMvc.perform(put("/api/gis-speeding-limits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gisSpeedingLimit)))
            .andExpect(status().isCreated());

        // Validate the GisSpeedingLimit in the database
        List<GisSpeedingLimit> gisSpeedingLimitList = gisSpeedingLimitRepository.findAll();
        assertThat(gisSpeedingLimitList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGisSpeedingLimit() throws Exception {
        // Initialize the database
        gisSpeedingLimitRepository.saveAndFlush(gisSpeedingLimit);
        int databaseSizeBeforeDelete = gisSpeedingLimitRepository.findAll().size();

        // Get the gisSpeedingLimit
        restGisSpeedingLimitMockMvc.perform(delete("/api/gis-speeding-limits/{id}", gisSpeedingLimit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<GisSpeedingLimit> gisSpeedingLimitList = gisSpeedingLimitRepository.findAll();
        assertThat(gisSpeedingLimitList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GisSpeedingLimit.class);
        GisSpeedingLimit gisSpeedingLimit1 = new GisSpeedingLimit();
        gisSpeedingLimit1.setId(1L);
        GisSpeedingLimit gisSpeedingLimit2 = new GisSpeedingLimit();
        gisSpeedingLimit2.setId(gisSpeedingLimit1.getId());
        assertThat(gisSpeedingLimit1).isEqualTo(gisSpeedingLimit2);
        gisSpeedingLimit2.setId(2L);
        assertThat(gisSpeedingLimit1).isNotEqualTo(gisSpeedingLimit2);
        gisSpeedingLimit1.setId(null);
        assertThat(gisSpeedingLimit1).isNotEqualTo(gisSpeedingLimit2);
    }
}
