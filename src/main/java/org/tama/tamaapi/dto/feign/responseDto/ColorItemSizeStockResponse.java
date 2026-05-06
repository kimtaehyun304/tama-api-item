package org.tama.tamaapi.dto.feign.responseDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.tama.tamaapi.domain.item.ColorItemSizeStock;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ColorItemSizeStockResponse {

    private Long id;

    private Long colorItemId;

    private String size;

    private int stock;

    public ColorItemSizeStockResponse(ColorItemSizeStock colorItemSizeStock) {
        id = colorItemSizeStock.getId();
        colorItemId = colorItemSizeStock.getColorItem().getId();
        size = colorItemSizeStock.getSize();
        stock = colorItemSizeStock.getStock();
    }

}
