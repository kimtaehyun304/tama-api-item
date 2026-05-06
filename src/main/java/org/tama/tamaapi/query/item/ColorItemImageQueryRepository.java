package org.tama.tamaapi.query.item;

import org.tama.tamaapi.domain.item.ColorItemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface ColorItemImageQueryRepository extends JpaRepository<ColorItemImage, Long> {

    List<ColorItemImage> findAllByColorItemId(Long colorItemId);

    List<ColorItemImage> findAllByColorItemIdIn(List<Long> colorItemIds);

    List<ColorItemImage> findAllByColorItemIdInAndSequence(List<Long> colorItemIds, Integer sequence);



}
