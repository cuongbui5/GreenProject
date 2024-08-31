package com.example.greenproject.service;

import com.example.greenproject.dto.req.UpdateVariationOptionRequest;
import com.example.greenproject.model.Variation;
import com.example.greenproject.model.VariationOption;
import com.example.greenproject.repository.VariationOptionRepository;
import com.example.greenproject.repository.VariationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VariationOptionService {
    private final VariationOptionRepository variationOptionRepository;

    public void deleteVariation(Long id){
        VariationOption existOrNotVariation = variationOptionRepository.findById(id).orElseThrow();
        existOrNotVariation.setVariation(null);
        variationOptionRepository.delete(existOrNotVariation);
    }

    public VariationOption updateVariationOption(Long id, UpdateVariationOptionRequest updateVariationOptionRequest){
        VariationOption existOrNotVariation = variationOptionRepository.findById(id).orElseThrow();
        existOrNotVariation.setValue(updateVariationOptionRequest.getValue());
        return variationOptionRepository.save(existOrNotVariation);
    }
}
