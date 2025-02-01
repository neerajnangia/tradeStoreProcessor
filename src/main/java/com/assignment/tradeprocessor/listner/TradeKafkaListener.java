package com.assignment.tradeprocessor.listner;

import com.assignment.tradeprocessor.entity.Trade;
import com.assignment.tradeprocessor.service.TradeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TradeKafkaListener {

    @Autowired
    private TradeService tradeService;

    @KafkaListener(topics = "trade_topic", groupId = "trade_group")
    public void listen(ConsumerRecord<String, String> record) throws JsonProcessingException {
        // Deserialize the trade record from the Kafka message
        Trade trade = deserializeTrade(record.value());

        // Process the trade
        tradeService.processTrade(trade);
    }

    private Trade deserializeTrade(String tradeJson) throws JsonProcessingException {
        // Logic to deserialize the JSON into a Trade object
        return new ObjectMapper().readValue(tradeJson, Trade.class);
    }
}