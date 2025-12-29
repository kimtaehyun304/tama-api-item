package org.example.tamaapi.event;

import lombok.RequiredArgsConstructor;
import org.example.tamaapi.command.item.ItemService;
import org.springframework.boot.autoconfigure.jms.AcknowledgeMode;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemEventConsumer {

    private final String ITEM_TOPIC = "item_topic";

    private final ItemService itemService;
    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 5000, multiplier = 2)
    )
    @KafkaListener(topics = ITEM_TOPIC, groupId = "item_consumer_group")
    public void consumeIncreaseStockEvent(IncreaseStockEvent event, Acknowledgment ack) {
        itemService.increaseStocks(event.getRequests());
        ack.acknowledge();
    }

}
