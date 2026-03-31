package org.example.tamaapi.query;

import org.example.tamaapi.domain.DecreaseStockLog;
import org.example.tamaapi.domain.StockLogStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface DecreaseStockLogQueryRepository extends JpaRepository<DecreaseStockLog, Long> {

    //이거 exists 말고 limit 1로 나가지만 성능은 같음
    boolean existsByPaymentId(String paymentId);

    List<DecreaseStockLog> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
