package com.assignment.tradeprocessor.scheduler;

import com.assignment.tradeprocessor.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TradeScheduler {
    @Autowired
    private TradeService tradeService;

    @Scheduled(cron = "0 0 0 * *") // Runs every day
    public void scheduleExpireTrades() {
        tradeService.expireTrades();
    }
}
