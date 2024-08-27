package com.example.greenproject.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateContactRequest {
    private String phoneNumber;
    private String address;
    private String name;
    private String email;
}
