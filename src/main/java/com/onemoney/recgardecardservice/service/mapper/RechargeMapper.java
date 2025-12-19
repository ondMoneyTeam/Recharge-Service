package com.onemoney.recgardecardservice.service.mapper;

import com.onemoney.recgardecardservice.domain.Card;
import com.onemoney.recgardecardservice.domain.Recharge;
import com.onemoney.recgardecardservice.service.dto.CardDTO;
import com.onemoney.recgardecardservice.service.dto.RechargeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Recharge} and its DTO {@link RechargeDTO}.
 */
@Mapper(componentModel = "spring")
public interface RechargeMapper extends EntityMapper<RechargeDTO, Recharge> {
    @Mapping(target = "card", source = "card", qualifiedByName = "cardId")
    RechargeDTO toDto(Recharge s);

    @Named("cardId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CardDTO toDtoCardId(Card card);
}
