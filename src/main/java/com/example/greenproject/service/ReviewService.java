package com.example.greenproject.service;

import com.example.greenproject.dto.req.ReviewRequest;
import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.dto.res.ReviewDto;
import com.example.greenproject.model.ProductItem;
import com.example.greenproject.model.Review;
import com.example.greenproject.model.User;
import com.example.greenproject.model.pk.ReviewId;
import com.example.greenproject.repository.ProductItemRepository;
import com.example.greenproject.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductItemRepository productItemRepository;
    private final UserService userService;

    public Object getReviewByProductId(Long productItemId,Integer pageNum, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNum-1,pageSize,Sort.by(Sort.Direction.DESC, "updatedAt"));
        Page<Review> reviews = reviewRepository.findByIdProductItemId(productItemId,pageable);

        return new PaginatedResponse<>(
                reviews.getContent().stream().map(Review::mapToReviewDto).toList(),
                reviews.getTotalPages(),
                reviews.getNumber() + 1,
                reviews.getTotalElements()
        );
    }

    public ReviewDto createReview(ReviewRequest reviewRequest){
        User user = userService.getUserInfo();
        ProductItem productItem = productItemRepository.findById(reviewRequest.getProductItemId())
                .orElseThrow(()->new RuntimeException("Not found product item id " + reviewRequest.getProductItemId()));

        ReviewId reviewId = new ReviewId();
        reviewId.setUser(user);
        reviewId.setProductItem(productItem);

        Review review = new Review();
        review.setId(reviewId);
        review.setRating(reviewRequest.getRating());
        review.setContent(reviewRequest.getContent());

        return reviewRepository.save(review).mapToReviewDto();
    }

    public ReviewDto updateReview(ReviewRequest reviewRequest){
        User user = userService.getUserInfo();
        ProductItem productItem = productItemRepository.findById(reviewRequest.getProductItemId())
                .orElseThrow(()->new RuntimeException("Not found product item id " + reviewRequest.getProductItemId()));

        ReviewId reviewId = new ReviewId();
        reviewId.setUser(user);
        reviewId.setProductItem(productItem);

        Review review = reviewRepository.findById(reviewId).orElseThrow(()->new RuntimeException("Not found"));

        review.setContent(review.getContent());
        review.setRating(review.getRating());

        return reviewRepository.save(review).mapToReviewDto();
    }

    public void deleteReview(Long productItemId){
        User user = userService.getUserInfo();
        ProductItem productItem = productItemRepository.findById(productItemId)
                .orElseThrow(()->new RuntimeException("Not found product item id " + productItemId));

        ReviewId reviewId = new ReviewId();
        reviewId.setUser(user);
        reviewId.setProductItem(productItem);

        reviewRepository.deleteById(reviewId);
    }
}
