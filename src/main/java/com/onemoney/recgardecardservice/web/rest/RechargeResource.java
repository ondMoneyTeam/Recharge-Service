package com.onemoney.recgardecardservice.web.rest;

import com.onemoney.recgardecardservice.repository.RechargeRepository;
import com.onemoney.recgardecardservice.service.RechargeService;
import com.onemoney.recgardecardservice.service.dto.RechargeDTO;
import com.onemoney.recgardecardservice.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.onemoney.recgardecardservice.domain.Recharge}.
 */
@RestController
@RequestMapping("/api/recharges")
public class RechargeResource {

    private static final Logger LOG = LoggerFactory.getLogger(RechargeResource.class);

    private static final String ENTITY_NAME = "oneMoneyRechargeCardServiceRecharge";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RechargeService rechargeService;

    private final RechargeRepository rechargeRepository;

    public RechargeResource(RechargeService rechargeService, RechargeRepository rechargeRepository) {
        this.rechargeService = rechargeService;
        this.rechargeRepository = rechargeRepository;
    }

    /**
     * {@code POST  /recharges} : Create a new recharge.
     *
     * @param rechargeDTO the rechargeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rechargeDTO, or with status {@code 400 (Bad Request)} if the recharge has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RechargeDTO> createRecharge(@Valid @RequestBody RechargeDTO rechargeDTO) throws URISyntaxException {
        LOG.debug("REST request to save Recharge : {}", rechargeDTO);
        if (rechargeDTO.getId() != null) {
            throw new BadRequestAlertException("A new recharge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        rechargeDTO = rechargeService.save(rechargeDTO);
        return ResponseEntity.created(new URI("/api/recharges/" + rechargeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, rechargeDTO.getId().toString()))
            .body(rechargeDTO);
    }

    /**
     * {@code PUT  /recharges/:id} : Updates an existing recharge.
     *
     * @param id the id of the rechargeDTO to save.
     * @param rechargeDTO the rechargeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rechargeDTO,
     * or with status {@code 400 (Bad Request)} if the rechargeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rechargeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RechargeDTO> updateRecharge(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RechargeDTO rechargeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Recharge : {}, {}", id, rechargeDTO);
        if (rechargeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rechargeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rechargeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        rechargeDTO = rechargeService.update(rechargeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rechargeDTO.getId().toString()))
            .body(rechargeDTO);
    }

    /**
     * {@code PATCH  /recharges/:id} : Partial updates given fields of an existing recharge, field will ignore if it is null
     *
     * @param id the id of the rechargeDTO to save.
     * @param rechargeDTO the rechargeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rechargeDTO,
     * or with status {@code 400 (Bad Request)} if the rechargeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rechargeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rechargeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RechargeDTO> partialUpdateRecharge(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RechargeDTO rechargeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Recharge partially : {}, {}", id, rechargeDTO);
        if (rechargeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rechargeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rechargeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RechargeDTO> result = rechargeService.partialUpdate(rechargeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rechargeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /recharges} : get all the recharges.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recharges in body.
     */
    @GetMapping("")
    public List<RechargeDTO> getAllRecharges() {
        LOG.debug("REST request to get all Recharges");
        return rechargeService.findAll();
    }

    /**
     * {@code GET  /recharges/:id} : get the "id" recharge.
     *
     * @param id the id of the rechargeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rechargeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RechargeDTO> getRecharge(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Recharge : {}", id);
        Optional<RechargeDTO> rechargeDTO = rechargeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rechargeDTO);
    }

    /**
     * {@code DELETE  /recharges/:id} : delete the "id" recharge.
     *
     * @param id the id of the rechargeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecharge(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Recharge : {}", id);
        rechargeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
