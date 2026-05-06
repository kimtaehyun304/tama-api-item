package org.tama.tamaapi.exception;

import org.tama.sharelib.common.exception.CustomFeignException;

public class NotEnoughStockException extends CustomFeignException {

    public NotEnoughStockException() {
        super("NOT_ENOUGH_STOCK", "재고가 부족합니다");
    }
}
