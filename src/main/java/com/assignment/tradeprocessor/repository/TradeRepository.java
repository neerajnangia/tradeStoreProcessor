package com.assignment.tradeprocessor.repository;

import com.assignment.tradeprocessor.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TradeRepository extends JpaRepository<Trade, String> {
    Optional<Trade> findByTradeId(String tradeId);

    Optional<Trade> findByTradeIdAndVersionId(String tradeId, int versionId);

    List<Trade> findByExpiredFalseAndMaturityDateBefore(LocalDate maturityDate);
}