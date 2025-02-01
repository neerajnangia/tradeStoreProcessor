package com.assignment.tradeprocessor.tradeprocessor;

import com.assignment.tradeprocessor.entity.Trade;
import com.assignment.tradeprocessor.repository.TradeRepository;
import com.assignment.tradeprocessor.service.TradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;


public class TradeProcessorApplicationTests {

    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    private TradeService tradeService;

    private Trade trade;

    @BeforeEach
    public void setUp() {
        AutoCloseable autoCloseable = openMocks(this);
        trade = new Trade();
        trade.setTradeId("T1");
        trade.setVersionId(1);
        trade.setCounterpartyId("CP1");
        trade.setBookId("B1");
        trade.setMaturityDate(LocalDate.now().plusDays(1));
        trade.setCreatedDate(LocalDate.now());
        trade.setExpired(Trade.ExpirationStatus.N);
    }

    @Test
    public void testProcessTradeWithLowerVersion() {
        Trade existingTrade = new Trade();
        existingTrade.setVersionId(2);
        existingTrade.setTradeId("T1");

        when(tradeRepository.findByTradeId("T1")).thenReturn(Optional.of(existingTrade));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            tradeService.processTrade(trade);
        });

        assertEquals("Trade version is lower than the existing version.", exception.getMessage());
    }

    @Test
    public void testProcessTradeWithSameVersion() {
        Trade existingTrade = new Trade();
        existingTrade.setVersionId(1);
        existingTrade.setTradeId("T1");

        when(tradeRepository.findByTradeId("T1")).thenReturn(Optional.of(existingTrade));

        tradeService.processTrade(trade);
        verify(tradeRepository, times(1)).save(existingTrade); // Ensure trade is saved (replaced)
    }

    @Test
    public void testProcessTradeWithPastMaturityDate() {
        trade.setMaturityDate(LocalDate.now().minusDays(1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            tradeService.processTrade(trade);
        });

        assertEquals("Trade maturity date is in the past.", exception.getMessage());
    }

    @Test
    public void testSchedulerMarkExpiredTrades() {
        Trade expiredTrade = new Trade();
        expiredTrade.setMaturityDate(LocalDate.now().minusDays(1));
        expiredTrade.setExpired(Trade.ExpirationStatus.N);

        when(tradeRepository.findByExpiredFalseAndMaturityDateBefore(LocalDate.now())).thenReturn(Arrays.asList(expiredTrade));
        tradeService.expireTrades();

        assertEquals(Trade.ExpirationStatus.Y, expiredTrade.getExpired());
        verify(tradeRepository, times(1)).save(expiredTrade);
    }
}