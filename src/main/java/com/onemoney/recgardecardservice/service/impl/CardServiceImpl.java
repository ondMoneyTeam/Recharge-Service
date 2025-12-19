package com.onemoney.recgardecardservice.service.impl;

import com.onemoney.recgardecardservice.domain.Card;
import com.onemoney.recgardecardservice.repository.CardRepository;
import com.onemoney.recgardecardservice.service.CardService;
import com.onemoney.recgardecardservice.service.dto.CardDTO;
import com.onemoney.recgardecardservice.service.mapper.CardMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.onemoney.recgardecardservice.domain.Card}.
 */
@Service
@Transactional
public class CardServiceImpl implements CardService {

    private static final Logger LOG = LoggerFactory.getLogger(CardServiceImpl.class);

    private final CardRepository cardRepository;

    private final CardMapper cardMapper;

    public CardServiceImpl(CardRepository cardRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
    }

    @Override
    public CardDTO save(CardDTO cardDTO) {
        LOG.debug("Request to save Card : {}", cardDTO);
        Card card = cardMapper.toEntity(cardDTO);
        card = cardRepository.save(card);
        return cardMapper.toDto(card);
    }

    @Override
    public CardDTO update(CardDTO cardDTO) {
        LOG.debug("Request to update Card : {}", cardDTO);
        Card card = cardMapper.toEntity(cardDTO);
        card = cardRepository.save(card);
        return cardMapper.toDto(card);
    }

    @Override
    public Optional<CardDTO> partialUpdate(CardDTO cardDTO) {
        LOG.debug("Request to partially update Card : {}", cardDTO);

        return cardRepository
            .findById(cardDTO.getId())
            .map(existingCard -> {
                cardMapper.partialUpdate(existingCard, cardDTO);

                return existingCard;
            })
            .map(cardRepository::save)
            .map(cardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardDTO> findAll() {
        LOG.debug("Request to get all Cards");
        return cardRepository.findAll().stream().map(cardMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CardDTO> findOne(Long id) {
        LOG.debug("Request to get Card : {}", id);
        return cardRepository.findById(id).map(cardMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Card : {}", id);
        cardRepository.deleteById(id);
    }
}
