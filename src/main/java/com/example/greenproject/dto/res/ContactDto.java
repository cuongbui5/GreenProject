package com.example.greenproject.dto.res;

import lombok.Data;

@Data
public class ContactDto {
    private Long id;
    private String city;
    private String district;
    private String ward;
    private String houseAddress;
    private String phoneNumber;
    private String fullName;
}
