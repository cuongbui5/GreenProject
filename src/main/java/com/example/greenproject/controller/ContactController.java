package com.example.greenproject.controller;

import com.example.greenproject.dto.req.CreateContactRequest;
import com.example.greenproject.model.Contact;
import com.example.greenproject.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;
    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody CreateContactRequest createContactRequest) {
        Contact savedContact = contactService.saveContact(createContactRequest);
        return ResponseEntity.ok(savedContact);
    }
}
