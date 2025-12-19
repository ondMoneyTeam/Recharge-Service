package com.onemoney.recgardecardservice.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.onemoney.recgardecardservice.domain.Recharge} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RechargeDTO implements Serializable {

    private Long id;

    @NotNull
    private Long accoundId;


    @NotNull
    private Double amount;

    @NotNull
    private String status;

    @NotNull
    private LocalDate createdDate;

    private CardDTO card;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccoundId() {
        return accoundId;
    }

    public void setAccoundId(Long accoundId) {
        this.accoundId = accoundId;
    }



    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public CardDTO getCard() {
        return card;
    }

    public void setCard(CardDTO card) {
        this.card = card;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RechargeDTO)) {
            return false;
        }

        RechargeDTO rechargeDTO = (RechargeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rechargeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RechargeDTO{" +
            "id=" + getId() +
            ", accoundId=" + getAccoundId() +
            ", amount=" + getAmount() +
            ", status='" + getStatus() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", card=" + getCard() +
            "}";
    }
}
