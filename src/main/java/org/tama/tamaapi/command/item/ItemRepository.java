package org.tama.tamaapi.command.item;

import org.tama.tamaapi.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
