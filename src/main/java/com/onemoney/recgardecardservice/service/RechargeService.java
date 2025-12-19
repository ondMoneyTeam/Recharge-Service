package com.onemoney.recgardecardservice.service;

import com.onemoney.recgardecardservice.service.dto.RechargeDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.onemoney.recgardecardservice.domain.Recharge}.
 */
public interface RechargeService {
    /**
     * Save a recharge.
     *
     * @param rechargeDTO the entity to save.
     * @return the persisted entity.
     */
    RechargeDTO save(RechargeDTO rechargeDTO);

    /**
     * Updates a recharge.
     *
     * @param rechargeDTO the entity to update.
     * @return the persisted entity.
     */
    RechargeDTO update(RechargeDTO rechargeDTO);

    /**
     * Partially updates a recharge.
     *
     * @param rechargeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RechargeDTO> partialUpdate(RechargeDTO rechargeDTO);

    /**
     * Get all the recharges.
     *
     * @return the list of entities.
     */
    List<RechargeDTO> findAll();

    /**
     * Get the "id" recharge.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RechargeDTO> findOne(Long id);

    /**
     * Delete the "id" recharge.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
