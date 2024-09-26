package com.example.greenproject.dto.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
    @NotNull
    private Long productItemId;
    @Min(value = 1)
    @Max(value = 5)
    private int rating;
    @NotBlank
    private String content;
}
