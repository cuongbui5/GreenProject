package com.example.greenproject.controller;

import com.example.greenproject.service.VariationOptionService;
import com.example.greenproject.service.VariationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/variation_options")
@RequiredArgsConstructor
public class VariationOptionController {
    private final VariationOptionService variationOptionService;


}
