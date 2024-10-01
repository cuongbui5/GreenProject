package com.example.greenproject.model;

import com.example.greenproject.dto.res.ContactDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "_contact")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Contact extends BaseEntity {
    //https://provinces.open-api.vn/api/?depth=2
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String city;
    private String district;
    private String ward;
    private String houseAddress;
    private String phoneNumber;
    private String fullName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;
    public ContactDto mapToContactDto() {
        ContactDto contactDto = new ContactDto();
        contactDto.setId(id);
        contactDto.setCity(city);
        contactDto.setDistrict(district);
        contactDto.setWard(ward);
        contactDto.setHouseAddress(houseAddress);
        contactDto.setPhoneNumber(phoneNumber);
        contactDto.setFullName(fullName);
        return contactDto;
    }

}
