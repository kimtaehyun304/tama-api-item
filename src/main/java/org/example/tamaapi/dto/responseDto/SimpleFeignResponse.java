package org.example.tamaapi.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimpleFeignResponse {
    private String code;
    private String message;
}
