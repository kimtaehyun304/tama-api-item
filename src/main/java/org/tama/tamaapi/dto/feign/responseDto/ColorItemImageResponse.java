package org.tama.tamaapi.dto.feign.responseDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.tama.sharelib.common.util.UploadFile;
import org.tama.tamaapi.domain.item.ColorItemImage;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ColorItemImageResponse {

    private Long id;

    private Long colorItemId;

    private UploadFile uploadFile;

    private Integer sequence;

    public ColorItemImageResponse(ColorItemImage colorItemImage) {
        id = colorItemImage.getId();
        colorItemId = colorItemImage.getColorItem().getId();
        uploadFile = colorItemImage.getUploadFile();
        sequence = colorItemImage.getSequence();
    }

}
