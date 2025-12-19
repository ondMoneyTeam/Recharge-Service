package com.onemoney.recgardecardservice.web.rest;

import static com.onemoney.recgardecardservice.domain.RechargeAsserts.*;
import static com.onemoney.recgardecardservice.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onemoney.recgardecardservice.IntegrationTest;
import com.onemoney.recgardecardservice.domain.Recharge;
import com.onemoney.recgardecardservice.repository.RechargeRepository;
import com.onemoney.recgardecardservice.service.dto.RechargeDTO;
import com.onemoney.recgardecardservice.service.mapper.RechargeMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RechargeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RechargeResourceIT {

    private static final Long DEFAULT_ACCOUND_ID = 1L;
    private static final Long UPDATED_ACCOUND_ID = 2L;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/recharges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RechargeRepository rechargeRepository;

    @Autowired
    private RechargeMapper rechargeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRechargeMockMvc;

    private Recharge recharge;

    private Recharge insertedRecharge;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recharge createEntity() {
        return new Recharge()
            .accoundId(DEFAULT_ACCOUND_ID)
            .amount(DEFAULT_AMOUNT)
            .status(DEFAULT_STATUS)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recharge createUpdatedEntity() {
        return new Recharge()
            .accoundId(UPDATED_ACCOUND_ID)
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        recharge = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRecharge != null) {
            rechargeRepository.delete(insertedRecharge);
            insertedRecharge = null;
        }
    }

    @Test
    @Transactional
    void createRecharge() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Recharge
        RechargeDTO rechargeDTO = rechargeMapper.toDto(recharge);
        var returnedRechargeDTO = om.readValue(
            restRechargeMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rechargeDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RechargeDTO.class
        );

        // Validate the Recharge in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRecharge = rechargeMapper.toEntity(returnedRechargeDTO);
        assertRechargeUpdatableFieldsEquals(returnedRecharge, getPersistedRecharge(returnedRecharge));

        insertedRecharge = returnedRecharge;
    }

    @Test
    @Transactional
    void createRechargeWithExistingId() throws Exception {
        // Create the Recharge with an existing ID
        recharge.setId(1L);
        RechargeDTO rechargeDTO = rechargeMapper.toDto(recharge);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRechargeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rechargeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Recharge in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAccoundIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recharge.setAccoundId(null);

        // Create the Recharge, which fails.
        RechargeDTO rechargeDTO = rechargeMapper.toDto(recharge);

        restRechargeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rechargeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }



    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recharge.setAmount(null);

        // Create the Recharge, which fails.
        RechargeDTO rechargeDTO = rechargeMapper.toDto(recharge);

        restRechargeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rechargeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recharge.setStatus(null);

        // Create the Recharge, which fails.
        RechargeDTO rechargeDTO = rechargeMapper.toDto(recharge);

        restRechargeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rechargeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recharge.setCreatedDate(null);

        // Create the Recharge, which fails.
        RechargeDTO rechargeDTO = rechargeMapper.toDto(recharge);

        restRechargeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rechargeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecharges() throws Exception {
        // Initialize the database
        insertedRecharge = rechargeRepository.saveAndFlush(recharge);

        // Get all the rechargeList
        restRechargeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recharge.getId().intValue())))
            .andExpect(jsonPath("$.[*].accoundId").value(hasItem(DEFAULT_ACCOUND_ID.intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getRecharge() throws Exception {
        // Initialize the database
        insertedRecharge = rechargeRepository.saveAndFlush(recharge);

        // Get the recharge
        restRechargeMockMvc
            .perform(get(ENTITY_API_URL_ID, recharge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recharge.getId().intValue()))
            .andExpect(jsonPath("$.accoundId").value(DEFAULT_ACCOUND_ID.intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRecharge() throws Exception {
        // Get the recharge
        restRechargeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecharge() throws Exception {
        // Initialize the database
        insertedRecharge = rechargeRepository.saveAndFlush(recharge);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recharge
        Recharge updatedRecharge = rechargeRepository.findById(recharge.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRecharge are not directly saved in db
        em.detach(updatedRecharge);
        updatedRecharge
            .accoundId(UPDATED_ACCOUND_ID)
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE);
        RechargeDTO rechargeDTO = rechargeMapper.toDto(updatedRecharge);

        restRechargeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rechargeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rechargeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Recharge in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRechargeToMatchAllProperties(updatedRecharge);
    }

    @Test
    @Transactional
    void putNonExistingRecharge() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recharge.setId(longCount.incrementAndGet());

        // Create the Recharge
        RechargeDTO rechargeDTO = rechargeMapper.toDto(recharge);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRechargeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rechargeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rechargeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recharge in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecharge() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recharge.setId(longCount.incrementAndGet());

        // Create the Recharge
        RechargeDTO rechargeDTO = rechargeMapper.toDto(recharge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRechargeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rechargeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recharge in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecharge() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recharge.setId(longCount.incrementAndGet());

        // Create the Recharge
        RechargeDTO rechargeDTO = rechargeMapper.toDto(recharge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRechargeMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rechargeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recharge in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRechargeWithPatch() throws Exception {
        // Initialize the database
        insertedRecharge = rechargeRepository.saveAndFlush(recharge);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recharge using partial update
        Recharge partialUpdatedRecharge = new Recharge();
        partialUpdatedRecharge.setId(recharge.getId());

        restRechargeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecharge.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecharge))
            )
            .andExpect(status().isOk());

        // Validate the Recharge in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRechargeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRecharge, recharge), getPersistedRecharge(recharge));
    }

    @Test
    @Transactional
    void fullUpdateRechargeWithPatch() throws Exception {
        // Initialize the database
        insertedRecharge = rechargeRepository.saveAndFlush(recharge);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recharge using partial update
        Recharge partialUpdatedRecharge = new Recharge();
        partialUpdatedRecharge.setId(recharge.getId());

        partialUpdatedRecharge
            .accoundId(UPDATED_ACCOUND_ID)
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE);

        restRechargeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecharge.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecharge))
            )
            .andExpect(status().isOk());

        // Validate the Recharge in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRechargeUpdatableFieldsEquals(partialUpdatedRecharge, getPersistedRecharge(partialUpdatedRecharge));
    }

    @Test
    @Transactional
    void patchNonExistingRecharge() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recharge.setId(longCount.incrementAndGet());

        // Create the Recharge
        RechargeDTO rechargeDTO = rechargeMapper.toDto(recharge);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRechargeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rechargeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rechargeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recharge in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecharge() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recharge.setId(longCount.incrementAndGet());

        // Create the Recharge
        RechargeDTO rechargeDTO = rechargeMapper.toDto(recharge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRechargeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rechargeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recharge in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecharge() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recharge.setId(longCount.incrementAndGet());

        // Create the Recharge
        RechargeDTO rechargeDTO = rechargeMapper.toDto(recharge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRechargeMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rechargeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recharge in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecharge() throws Exception {
        // Initialize the database
        insertedRecharge = rechargeRepository.saveAndFlush(recharge);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the recharge
        restRechargeMockMvc
            .perform(delete(ENTITY_API_URL_ID, recharge.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return rechargeRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Recharge getPersistedRecharge(Recharge recharge) {
        return rechargeRepository.findById(recharge.getId()).orElseThrow();
    }

    protected void assertPersistedRechargeToMatchAllProperties(Recharge expectedRecharge) {
        assertRechargeAllPropertiesEquals(expectedRecharge, getPersistedRecharge(expectedRecharge));
    }

    protected void assertPersistedRechargeToMatchUpdatableProperties(Recharge expectedRecharge) {
        assertRechargeAllUpdatablePropertiesEquals(expectedRecharge, getPersistedRecharge(expectedRecharge));
    }
}
