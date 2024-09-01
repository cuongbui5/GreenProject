package com.example.greenproject.dto.req;

import com.example.greenproject.model.Variation;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVariationOptionRequest {
    private String value;
}
