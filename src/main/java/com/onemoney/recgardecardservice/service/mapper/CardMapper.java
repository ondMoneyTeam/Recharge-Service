package com.onemoney.recgardecardservice.service.mapper;

import com.onemoney.recgardecardservice.domain.Card;
import com.onemoney.recgardecardservice.service.dto.CardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Card} and its DTO {@link CardDTO}.
 */
@Mapper(componentModel = "spring")
public interface CardMapper extends EntityMapper<CardDTO, Card> {}
