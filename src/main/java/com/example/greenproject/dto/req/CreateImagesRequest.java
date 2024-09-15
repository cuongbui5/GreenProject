package com.example.greenproject.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Data
@Getter
public class CreateImagesRequest {
    private Long productId;
    private List<String> images;
}
