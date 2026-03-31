package org.example.tamaapi.domain;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//row가 있으면 재고 차감이 된거라고 판단 (트랜잭션 묶어논거라)
public class DecreaseStockLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "decrease_stock_log_id")
    private Long id;

    //private Long orderId;

    private String paymentId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private JsonNode payload;

    //역할 다하면 삭제하는 방식이라 안 씀
    //private StockLogStatus status;

    public DecreaseStockLog(String paymentId, JsonNode payload) {
        this.paymentId = paymentId;
        this.payload = payload;
        //this.status = StockLogStatus.DECREASED;
    }
}