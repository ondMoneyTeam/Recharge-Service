package com.onemoney.recgardecardservice.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.onemoney.recgardecardservice.domain.Card} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CardDTO implements Serializable {

    private Long id;

    @NotNull
    private Long accountId;

    @NotNull
    private String cardNumber;

    private String cardType;

    private BigDecimal balance;

    @NotNull
    private Boolean status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardDTO)) {
            return false;
        }

        CardDTO cardDTO = (CardDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cardDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardDTO{" +
            "id=" + getId() +
            ", accountId=" + getAccountId() +
            ", cardNumber='" + getCardNumber() + "'" +
            ", cardType='" + getCardType() + "'" +
            ", balance=" + getBalance() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
