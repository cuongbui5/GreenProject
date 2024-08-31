package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateVariationOptionRequest;
import com.example.greenproject.dto.req.CreateVariationRequest;
import com.example.greenproject.dto.req.UpdateVariationRequest;
import com.example.greenproject.model.Category;
import com.example.greenproject.model.Variation;
import com.example.greenproject.model.VariationOption;
import com.example.greenproject.repository.CategoryRepository;
import com.example.greenproject.repository.VariationOptionRepository;
import com.example.greenproject.repository.VariationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VariationService {
    private final VariationRepository variationRepository;
    private final CategoryRepository categoryRepository;
    private final VariationOptionRepository variationOptionRepository;

    public List<Variation> getAllVariation(){
        return variationRepository.findAll();
    }

    public List<Variation> getVariationByCategoryId(Long id){
        return variationRepository.getAllVariationByCategoryId(id);
    }

    public Variation createVariation(CreateVariationRequest createVariationRequest){
        String variationName = createVariationRequest.getName();
        Variation existOrNotVariation = variationRepository.findByName(variationName)
                .orElse(Variation
                        .builder()
                        .name(variationName)
                        .build());
        if(existOrNotVariation.getId() == null){
            return variationRepository.save(existOrNotVariation);
        }
        throw new RuntimeException("Already exist variation");
    }


    public Variation updateVariation(Long variationId, UpdateVariationRequest updateVariationRequest){
        Variation existOrNotVariation = variationRepository.findById(variationId).orElseThrow();
        existOrNotVariation.setName(updateVariationRequest.getName());
        return variationRepository.save(existOrNotVariation);
    }



    public VariationOption addVariationOptionToVariation(Long variationId, CreateVariationOptionRequest createVariationOptionRequest){
        Variation variation = variationRepository.findById(variationId).orElseThrow();

        String variationOptionValue = createVariationOptionRequest.getValue();
        VariationOption existOrNotVariationOption = variationOptionRepository.findByValue(variationOptionValue)
                .orElse(VariationOption
                        .builder()
                        .value(variationOptionValue)
                        .variation(variation)
                        .build());
        if(existOrNotVariationOption.getId() == null){
            variation.getVariationOptions().add(existOrNotVariationOption);
            Variation saveVariation = variationRepository.save(variation);
            return saveVariation.getVariationOptions().stream()
                    .filter(variationOption -> variationOption.getValue().equals(variationOptionValue))
                    .findFirst().orElseThrow();
        }
        throw new RuntimeException();
    }

}