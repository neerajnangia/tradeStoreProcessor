package com.assignment.tradeprocessor.service;

import com.assignment.tradeprocessor.entity.Trade;
import com.assignment.tradeprocessor.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    public void processTrade(Trade incomingTrade) {
        if (incomingTrade.getMaturityDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Trade maturity date is in the past.");
        }

        Optional<Trade> existingTradeOpt = tradeRepository.findByTradeId(incomingTrade.getTradeId());

        if (existingTradeOpt.isPresent()) {
            Trade existingTrade = existingTradeOpt.get();

            // Reject if incoming trade version is lower
            if (incomingTrade.getVersionId() < existingTrade.getVersionId()) {
                throw new IllegalArgumentException("Trade version is lower than the existing version.");
            }

            // Replace trade with same version
            if (incomingTrade.getVersionId() == existingTrade.getVersionId()) {
                existingTrade.setMaturityDate(incomingTrade.getMaturityDate());
                existingTrade.setCounterpartyId(incomingTrade.getCounterpartyId());
                existingTrade.setBookId(incomingTrade.getBookId());
                existingTrade.setCreatedDate(incomingTrade.getCreatedDate());
                tradeRepository.save(existingTrade);
            }
        } else {
            // Save new trade if no existing trade is found
            incomingTrade.setExpired(Trade.ExpirationStatus.N);
            tradeRepository.save(incomingTrade);
        }
    }


    public void expireTrades() {
        List<Trade> trades = tradeRepository.findByExpiredFalseAndMaturityDateBefore(LocalDate.now());

        for (Trade trade : trades) {
            trade.setExpired(Trade.ExpirationStatus.N);
            tradeRepository.save(trade);
        }

    }
}