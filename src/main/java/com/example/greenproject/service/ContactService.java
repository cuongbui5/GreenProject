package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateContactRequest;
import com.example.greenproject.dto.res.ContactDto;
import com.example.greenproject.model.Contact;
import com.example.greenproject.model.User;
import com.example.greenproject.repository.ContactRepository;
import com.example.greenproject.security.UserInfo;
import com.example.greenproject.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final UserService userService;

    // Create or Update a Contact
    public ContactDto saveContact(CreateContactRequest createContactRequest) {
        User user=userService.getUserByUserInfo();
        Contact contact = new Contact();
        contact.setCity(createContactRequest.getCity());
        contact.setWard(createContactRequest.getWard());
        contact.setDistrict(createContactRequest.getDistrict());
        contact.setHouseAddress(createContactRequest.getHouseAddress());
        contact.setPhoneNumber(createContactRequest.getPhoneNumber());
        contact.setFullName(createContactRequest.getFullName());
        contact.setUser(user);
        return contactRepository.save(contact).mapToContactDto();
    }


    public List<ContactDto> getAllContactsByUser() {
        UserInfo userInfo= Utils.getUserInfoFromContext();
        return contactRepository.findContactByUserId(userInfo.getId()).stream().map(Contact::mapToContactDto).toList();
    }




    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }
}
