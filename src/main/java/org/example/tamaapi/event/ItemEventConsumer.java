package org.example.tamaapi.event;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.tamaapi.command.item.ItemRepository;
import org.example.tamaapi.command.item.ItemService;
import org.example.tamaapi.query.DecreaseStockLogRepository;
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
    private final DecreaseStockLogRepository decreaseStockLogRepository;

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 3000, multiplier = 2)
    )
    @KafkaListener(topics = ITEM_TOPIC, groupId = "item_consumer_group")
    //retry + ack 인데 왜 되지? order msa에선 안됐는데
    @Transactional
    public void consumeIncreaseStockEvent(IncreaseStockEvent event, Acknowledgment ack) {
        //재고 차감 → 쿠폰 적용 → 주문 저장 순이라, order 저장 전이라 order 조회 불가하여 zero payload 불가

        //재고 차감안됐는데 타임아웃만 난 경우도 있어서 체크헤야함
        if(decreaseStockLogRepository.existsByPaymentId(event.getPaymentId())) {
            itemService.increaseStocks(event.getRequests());
            itemService.deleteDecreaseStockLog(event.getPaymentId());
        }
        //재고 차감안된경우도 정상케이스라 커밋
        ack.acknowledge();
    }

}