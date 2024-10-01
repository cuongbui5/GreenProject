package com.example.greenproject.controller;

import com.example.greenproject.dto.req.CreateContactRequest;
import com.example.greenproject.dto.res.BaseResponse;
import com.example.greenproject.dto.res.DataResponse;
import com.example.greenproject.model.Contact;
import com.example.greenproject.service.ContactService;
import com.example.greenproject.utils.Constants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;
    @PostMapping("/create")
    public ResponseEntity<?> createContact(@RequestBody CreateContactRequest createContactRequest) {

        return ResponseEntity.ok().body(new DataResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE,
                contactService.saveContact(createContactRequest)
        ));
    }

    @GetMapping("/getByUser")
    public ResponseEntity<?> getALlContactByUserId() {

        return ResponseEntity.ok().body(new DataResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE,
                contactService.getAllContactsByUser()
        ));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.ok().body(new BaseResponse(
                HttpStatus.OK.value(),
                Constants.SUCCESS_MESSAGE

        ));
    }
}
