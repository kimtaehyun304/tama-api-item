package org.tama.tamaapi.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.tama.tamaapi.dto.feign.requestDto.ItemOrderCountRequest;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class IncreaseStockEvent {

    private final String eventType = "INCREASE_STOCK";
    private String paymentId;
    private List<ItemOrderCountRequest> requests;

}
