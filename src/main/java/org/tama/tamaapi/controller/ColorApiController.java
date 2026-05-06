package org.tama.tamaapi.controller;

import lombok.RequiredArgsConstructor;
import org.tama.tamaapi.domain.item.Color;
import org.tama.tamaapi.dto.responseDto.color.ColorResponse;
import org.tama.tamaapi.dto.responseDto.color.ParentColorResponse;
import org.tama.tamaapi.query.item.ColorQueryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
//카테고리 아이템은 itemApi
public class ColorApiController {
    private final ColorQueryRepository colorQueryRepository;

    @GetMapping("/api/colors/parent")
    public List<ParentColorResponse> parentColors() {
        List<Color> colors = colorQueryRepository.findAllByParentIsNull();
        return colors.stream().map(ParentColorResponse::new).toList();
    }

    @GetMapping("/api/colors")
    public List<ColorResponse> colors() {
        List<Color> colors = colorQueryRepository.findAllWithChildrenByParentIsNull();
        return colors.stream().map(ColorResponse::new).toList();
    }

}
