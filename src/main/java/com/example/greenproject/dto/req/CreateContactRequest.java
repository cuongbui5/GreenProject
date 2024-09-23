package com.example.greenproject.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateContactRequest {
    private String city;
    private String district;
    private String ward;
    private String houseNumber;
}
