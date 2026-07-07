package org.tama.tamaapi.scheduler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tama.tamaapi.command.item.ItemService;
import org.tama.tamaapi.domain.DecreaseStockLog;
import org.tama.tamaapi.dto.feign.requestDto.ItemOrderCountRequest;
import org.tama.tamaapi.feignClient.order.OrderFeignClient;
import org.tama.tamaapi.query.DecreaseStockLogQueryRepository;
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
    //1. 주문 실패하여 자동으로 재고 롤백해야하는데, 서버 다운돼서 롤백 못한거 롤백
    //2. 주문이 완료된 재고 차감 로그 삭제 (의도한대로 및 정상적으로 끝난 케이스)

    //장애는 가끔 일어나니까, db 부하를 줄이기 위해 1시간 스케줄링 주기가 적당하다고 판단
    //fixedDelay는 앱 시작시에 바로 실행
    @Scheduled(fixedDelay = 1000*60*60, zone = "Asia/Seoul")
    public void checkAndRollbackStock(){
        //상품 ec2,rds 다운 시간을 최대 3시간으로 판단 (그 전에 장애나는건 수동으로 처리)
        LocalDateTime start = LocalDateTime.now().minusHours(3);
        //주문 차감 단계 까지만 진행되고 있는 주문이 있을 수 있어서 10분 전까지로 설정
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
        //루프 돌려서 row 마다 increase 실패하면 delete도 실패하게 하려고
        for (DecreaseStockLog deleteLog : deleteLogs) {
            //convertValue 평탄화하면 increase, delete 못 묶어서 이렇게 함
            List<ItemOrderCountRequest> requests = objectMapper.convertValue(deleteLog.getPayload(), new TypeReference<>() {});
            itemService.increaseStockAndDeleteLog(requests, deleteLog.getPaymentId());
        }
    }

}
