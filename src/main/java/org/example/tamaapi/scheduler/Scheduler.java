package org.example.tamaapi.scheduler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tamaapi.command.item.ItemService;
import org.example.tamaapi.domain.DecreaseStockLog;
import org.example.tamaapi.dto.feign.requestDto.ItemOrderCountRequest;
import org.example.tamaapi.feignClient.order.OrderFeignClient;
import org.example.tamaapi.query.DecreaseStockLogQueryRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
@RequiredArgsConstructor
@Slf4j
public class Scheduler {

    private final OrderFeignClient orderFeignClient;
    private final DecreaseStockLogQueryRepository decreaseStockLogQueryRepository;
    private final ItemService itemService;
    private final ObjectMapper objectMapper;

    //역할
    //1. 재고 차감됐는데, 주문 저장전에 서버 down돼서 재고 롤백 안 된거 롤백
    //2. 주문이 완료된 재고 차감 로그 삭제 (의도한 케이스)

    //1시간 주기가 적당한듯 (장애는 흔하지 않으니까)
    //fixedDelay는 앱 시작시에 바로 실행
    @Scheduled(fixedDelay = 1000*60*60, zone = "Asia/Seoul")
    public void checkAndRollbackStock(){
        //3시간동안 상품 서버 down 될 가능성 고려 (더 길게 장애나는건 수동으로 처리)
        //토스뱅크는 지연되면 알람오게 하더라
        LocalDateTime start = LocalDateTime.now().minusHours(3);
        //최근 주문은 아직 진행 중이라, 재고 차감 단계 까지만 진행된 걸 수 있어서 제외
        LocalDateTime end = LocalDateTime.now().minusMinutes(10);

        //3시간 전 이후 row 조회 == 3시간 이내 row
        List<DecreaseStockLog> logs = decreaseStockLogQueryRepository.findByCreatedAtBetween(start, end);
        List<String> paymentIds = logs.stream().map(DecreaseStockLog::getPaymentId).toList();
        Set<String> orderedPaymentIds = new HashSet<>(orderFeignClient.findExistingPaymentIds(paymentIds));

        Set<String> orderedSet = new HashSet<>(orderedPaymentIds);

        //원래 삭제해야할 로그
        List<DecreaseStockLog> orderLogs = new ArrayList<>();

        //재고 롤백후 삭제할 로그
        List<DecreaseStockLog> deleteLogs = new ArrayList<>();

        for (DecreaseStockLog log : logs) {
            if (orderedSet.contains(log.getPaymentId())) orderLogs.add(log);
            else deleteLogs.add(log);
        }

        //원래 삭제해야할 로그
        List<String> orderLogPaymentIds = orderLogs.stream().map(DecreaseStockLog::getPaymentId).toList();
        itemService.deleteDecreaseStockLogInPaymentIds(orderLogPaymentIds);

        //재고 롤백후 삭제할 로그
        for (DecreaseStockLog deleteLog : deleteLogs) {
            //convertValue 루프 돌려서 row 마다 increase 실패하면 delete도 실패하게 하려고
            List<ItemOrderCountRequest> requests = objectMapper.convertValue(deleteLog.getPayload(), new TypeReference<>() {});
            itemService.increaseStockAndDeleteLog(requests, deleteLog.getPaymentId());
        }
    }

}
