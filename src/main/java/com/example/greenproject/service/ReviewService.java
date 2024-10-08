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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductItemRepository productItemRepository;
    private final UserService userService;

    @Transactional
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

    public ReviewDto getReviewByReviewId(Long productItemId){
        User user = userService.getUserByUserInfo();
        ProductItem productItem = productItemRepository.findById(productItemId)
                .orElseThrow(()->new RuntimeException("Not found product item id " + productItemId));
        ReviewId reviewId = new ReviewId();
        reviewId.setUser(user);
        reviewId.setProductItem(productItem);

        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        return reviewOptional.map(Review::mapToReviewDto).orElse(null);
    }

    public ReviewDto createReview(ReviewRequest reviewRequest){
        User user = userService.getUserByUserInfo();
        ProductItem productItem = productItemRepository.findById(reviewRequest.getProductItemId())
                .orElseThrow(()->new RuntimeException("Not found product item id " + reviewRequest.getProductItemId()));

        ReviewId reviewId = new ReviewId();
        reviewId.setUser(user);
        reviewId.setProductItem(productItem);
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);

        if(reviewOptional.isEmpty()){
            Review review = new Review();
            review.setId(reviewId);
            review.setRating(reviewRequest.getRating());
            review.setContent(reviewRequest.getContent());
            productItem.setTotalRating(productItem.getTotalRating() + reviewRequest.getRating());
            productItem.setReviewsCount(productItem.getReviewsCount() + 1);
            productItemRepository.save(productItem);
            return reviewRepository.save(review).mapToReviewDto();
        }
        Review review = reviewOptional.get();
        productItem.setTotalRating(productItem.getTotalRating()+reviewRequest.getRating()-review.getRating());
        productItemRepository.save(productItem);
        review.setRating(reviewRequest.getRating());
        review.setContent(reviewRequest.getContent());
        return reviewRepository.save(review).mapToReviewDto();




    }

    public Review findByReviewId(Long productItemId){
        User user = userService.getUserByUserInfo();
        ProductItem productItem = productItemRepository.findById(productItemId)
                .orElseThrow(()->new RuntimeException("Not found product item id "));
        ReviewId reviewId = new ReviewId();
        reviewId.setUser(user);
        reviewId.setProductItem(productItem);
        return reviewRepository.findById(reviewId).orElseThrow(()->new RuntimeException("Not found"));

    }

   @Transactional
    public void deleteReview(Long productItemId){
        Review review = findByReviewId(productItemId);
        reviewRepository.delete(review);
        ProductItem productItem=review.getId().getProductItem();
        productItem.setReviewsCount(productItem.getReviewsCount() - 1);
        productItem.setTotalRating(productItem.getTotalRating()-review.getRating());
        productItemRepository.save(productItem);
    }

}
