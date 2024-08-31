package com.example.greenproject.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateImageRequest {
    private Long productId;
    private String imageUrl;
}
