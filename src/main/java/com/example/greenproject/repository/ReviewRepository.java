package com.example.greenproject.repository;


import com.example.greenproject.model.Review;
import com.example.greenproject.model.pk.ReviewId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review,ReviewId> {

    Page<Review> findByIdProductItemId(Long productItemId,Pageable pageable);
}
