package com.example.greenproject.dto.res;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ReviewDto {
    private Long productItemId;
    private UserDtoLazy user;
    private String content;
    private int rating;
    private ZonedDateTime updatedAt;
}
