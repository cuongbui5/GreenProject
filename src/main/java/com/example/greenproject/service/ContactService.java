package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateContactRequest;
import com.example.greenproject.model.Contact;
import com.example.greenproject.model.User;
import com.example.greenproject.repository.ContactRepository;
import com.example.greenproject.repository.UserRepository;
import com.example.greenproject.security.UserInfo;
import com.example.greenproject.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final UserService userService;

    // Create or Update a Contact
    public Contact saveContact(CreateContactRequest createContactRequest) {
        UserInfo userInfo= Utils.getUserInfoFromContext();
        User user=userService.getUserById(userInfo.getId());
        Contact contact = new Contact();
        contact.setCity(createContactRequest.getCity());
        contact.setWard(createContactRequest.getWard());
        contact.setDistrict(createContactRequest.getDistrict());
        contact.setHouseNumber(createContactRequest.getHouseNumber());
        contact.setUser(user);

        return contactRepository.save(contact);
    }

    // Get all Contacts
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    // Get a Contact by ID
    public Optional<Contact> getContactById(Long id) {
        return contactRepository.findById(id);
    }

    // Update a Contact
    public Contact updateContact(Long id, Contact contactDetails) {


        return null;
    }

    // Delete a Contact
    public void deleteContact(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));

        contactRepository.delete(contact);
    }
}
