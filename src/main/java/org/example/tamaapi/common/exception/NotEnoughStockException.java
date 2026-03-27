package org.example.tamaapi.common.exception;

import feign.FeignException;
import lombok.Getter;
import org.example.tamaapi.common.exception.feign.CustomFeignException;


public class NotEnoughStockException extends CustomFeignException {

    public NotEnoughStockException() {
        super("NOT_ENOUGH_STOCK", "재고가 부족합니다");
    }
}
