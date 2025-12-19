package com.onemoney.recgardecardservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A Card.
 */
@Entity
@Table(name = "card")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "account_id", nullable = false )
    private Long accountId;

    @NotNull
    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "balance", precision = 21, scale = 2)
    private BigDecimal balance;

    @NotNull
    @Column(name = "status", nullable = false)
    private Boolean status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "card")
    @JsonIgnoreProperties(value = { "card" }, allowSetters = true)
    private Set<Recharge> recharges = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Card id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public Card accountId(Long accountId) {
        this.setAccountId(accountId);
        return this;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public Card cardNumber(String cardNumber) {
        this.setCardNumber(cardNumber);
        return this;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardType() {
        return this.cardType;
    }

    public Card cardType(String cardType) {
        this.setCardType(cardType);
        return this;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public Card balance(BigDecimal balance) {
        this.setBalance(balance);
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public Card status(Boolean status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Set<Recharge> getRecharges() {
        return this.recharges;
    }

    public void setRecharges(Set<Recharge> recharges) {
        if (this.recharges != null) {
            this.recharges.forEach(i -> i.setCard(null));
        }
        if (recharges != null) {
            recharges.forEach(i -> i.setCard(this));
        }
        this.recharges = recharges;
    }

    public Card recharges(Set<Recharge> recharges) {
        this.setRecharges(recharges);
        return this;
    }

    public Card addRecharge(Recharge recharge) {
        this.recharges.add(recharge);
        recharge.setCard(this);
        return this;
    }

    public Card removeRecharge(Recharge recharge) {
        this.recharges.remove(recharge);
        recharge.setCard(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Card)) {
            return false;
        }
        return getId() != null && getId().equals(((Card) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Card{" +
            "id=" + getId() +
            ", accountId=" + getAccountId() +
            ", cardNumber='" + getCardNumber() + "'" +
            ", cardType='" + getCardType() + "'" +
            ", balance=" + getBalance() +
            ", status='" + getStatus() + "'" +
            "}";
    }

    public boolean isStatus() { return status;
    }
}
