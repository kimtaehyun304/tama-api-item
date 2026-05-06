package org.tama.tamaapi.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class ItemCreatedEvent {

    private String eventType = "ITEM_CREATED";
    private Long itemId;

    public ItemCreatedEvent(Long itemId) {
        this.itemId = itemId;
    }
}
