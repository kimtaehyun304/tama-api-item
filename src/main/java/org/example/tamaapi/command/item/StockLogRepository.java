package org.example.tamaapi.command.item;

import org.example.tamaapi.domain.DecreaseStockLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockLogRepository extends JpaRepository<DecreaseStockLog, Long> {

}
