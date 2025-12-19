package com.onemoney.recgardecardservice.service.impl;

import com.onemoney.recgardecardservice.domain.Recharge;
import com.onemoney.recgardecardservice.repository.RechargeRepository;
import com.onemoney.recgardecardservice.service.RechargeService;
import com.onemoney.recgardecardservice.service.dto.RechargeDTO;
import com.onemoney.recgardecardservice.service.mapper.RechargeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.onemoney.recgardecardservice.domain.Recharge}.
 */
@Service
@Transactional
public class RechargeServiceImpl implements RechargeService {

    private static final Logger LOG = LoggerFactory.getLogger(RechargeServiceImpl.class);

    private final RechargeRepository rechargeRepository;

    private final RechargeMapper rechargeMapper;

    public RechargeServiceImpl(RechargeRepository rechargeRepository, RechargeMapper rechargeMapper) {
        this.rechargeRepository = rechargeRepository;
        this.rechargeMapper = rechargeMapper;
    }

    @Override
    public RechargeDTO save(RechargeDTO rechargeDTO) {
        LOG.debug("Request to save Recharge : {}", rechargeDTO);
        Recharge recharge = rechargeMapper.toEntity(rechargeDTO);
        recharge = rechargeRepository.save(recharge);
        return rechargeMapper.toDto(recharge);
    }

    @Override
    public RechargeDTO update(RechargeDTO rechargeDTO) {
        LOG.debug("Request to update Recharge : {}", rechargeDTO);
        Recharge recharge = rechargeMapper.toEntity(rechargeDTO);
        recharge = rechargeRepository.save(recharge);
        return rechargeMapper.toDto(recharge);
    }

    @Override
    public Optional<RechargeDTO> partialUpdate(RechargeDTO rechargeDTO) {
        LOG.debug("Request to partially update Recharge : {}", rechargeDTO);

        return rechargeRepository
            .findById(rechargeDTO.getId())
            .map(existingRecharge -> {
                rechargeMapper.partialUpdate(existingRecharge, rechargeDTO);

                return existingRecharge;
            })
            .map(rechargeRepository::save)
            .map(rechargeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RechargeDTO> findAll() {
        LOG.debug("Request to get all Recharges");
        return rechargeRepository.findAll().stream().map(rechargeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RechargeDTO> findOne(Long id) {
        LOG.debug("Request to get Recharge : {}", id);
        return rechargeRepository.findById(id).map(rechargeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Recharge : {}", id);
        rechargeRepository.deleteById(id);
    }
}
