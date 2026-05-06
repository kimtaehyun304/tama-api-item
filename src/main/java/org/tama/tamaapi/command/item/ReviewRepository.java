package org.tama.tamaapi.command.item;

import org.tama.tamaapi.domain.item.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByOrderItemId(Long orderItemId);

}
