package com.onemoney.recgardecardservice.repository;

import com.onemoney.recgardecardservice.domain.Recharge;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Recharge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RechargeRepository extends JpaRepository<Recharge, Long> {}
