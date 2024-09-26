package com.example.greenproject.model;

import com.example.greenproject.dto.res.ReviewDto;
import com.example.greenproject.model.pk.ReviewId;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "_review")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Review extends BaseEntity{
    @Id
    private ReviewId id;
    private String content;
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    public ReviewDto mapToReviewDto(){
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setUser(id.getUser().mapToUserDtoLazy());
        reviewDto.setRating(rating);
        reviewDto.setContent(content);
        reviewDto.setProductItemId(id.getProductItem().getId());
        reviewDto.setUpdatedAt(getUpdatedAt());
        return reviewDto;
    }
}
