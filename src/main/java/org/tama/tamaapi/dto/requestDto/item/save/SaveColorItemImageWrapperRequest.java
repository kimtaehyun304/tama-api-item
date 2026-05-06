package org.tama.tamaapi.dto.requestDto.item.save;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SaveColorItemImageWrapperRequest {

    @Valid
    @NotEmpty
    private List<SaveColorItemImageRequest> requests;
}
