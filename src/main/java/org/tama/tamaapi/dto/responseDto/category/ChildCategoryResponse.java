package org.tama.tamaapi.dto.responseDto.category;

import lombok.Getter;
import org.tama.tamaapi.domain.item.Category;

@Getter
public class ChildCategoryResponse {
    private final Long id;

    private final String name;

    public ChildCategoryResponse(Category category) {
        id = category.getId();
        name = category.getName();
    }
}
