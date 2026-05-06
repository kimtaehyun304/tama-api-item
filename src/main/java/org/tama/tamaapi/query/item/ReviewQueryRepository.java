package org.tama.tamaapi.query.item;

import org.tama.tamaapi.domain.item.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface ReviewQueryRepository extends JpaRepository<Review, Long> {


    Optional<Review> findByOrderItemId(Long orderItemId);

}
