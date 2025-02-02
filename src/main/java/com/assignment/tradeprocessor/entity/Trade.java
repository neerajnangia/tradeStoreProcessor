package com.assignment.tradeprocessor.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

@Entity
public class Trade {

    @jakarta.persistence.Id
    private String tradeId;
    private int versionId;
    private String counterpartyId;
    private String bookId;
    private LocalDate maturityDate;
    private LocalDate createdDate;
    @Enumerated(EnumType.STRING)
    private ExpirationStatus expired;

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public int getVersionId() {
        return versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    public String getCounterpartyId() {
        return counterpartyId;
    }

    public void setCounterpartyId(String counterpartyId) {
        this.counterpartyId = counterpartyId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public ExpirationStatus getExpired() {
        return expired;
    }

    public void setExpired(ExpirationStatus expired) {
        this.expired = expired;
    }


    public enum ExpirationStatus {
        Y, N
    }

    // Getters and Setters
}

