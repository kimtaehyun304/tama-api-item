package org.tama.tamaapi.dto.feign.responseDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.tama.tamaapi.domain.item.ColorItem;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ColorItemResponse {

    private Long id;

    private Long itemId;

    private Long colorId;

    public ColorItemResponse(ColorItem colorItem) {
        id = colorItem.getId();
        itemId = colorItem.getItem().getId();
        colorId = colorItem.getColor().getId();
    }

}
