package org.example.tamaapi.query;

import org.example.tamaapi.domain.DecreaseStockLog;
import org.example.tamaapi.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface DecreaseStockLogRepository extends JpaRepository<DecreaseStockLog, Long> {

    //이거 exists 말고 limit 1로 나가지만 성능은 같음
    boolean existsByPaymentId(String paymentId);

}
