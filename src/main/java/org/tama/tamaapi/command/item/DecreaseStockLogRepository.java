package org.tama.tamaapi.command.item;

import org.tama.tamaapi.domain.DecreaseStockLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DecreaseStockLogRepository extends JpaRepository<DecreaseStockLog, Long> {


}
