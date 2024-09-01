package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateVariationOptionRequest;
import com.example.greenproject.dto.req.UpdateVariationOptionRequest;
import com.example.greenproject.model.Variation;
import com.example.greenproject.model.VariationOption;
import com.example.greenproject.repository.VariationOptionRepository;
import com.example.greenproject.repository.VariationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VariationOptionService {
    private final VariationOptionRepository variationOptionRepository;
    private final VariationRepository variationRepository;

    public List<VariationOption> getVariationOptionByVariationId(Long variationId){
        return variationOptionRepository.findByVariationId(variationId);
    }

    public void deleteVariation(Long id){
        VariationOption existOrNotVariationOption = variationOptionRepository.findById(id).orElseThrow();
        existOrNotVariationOption.setVariation(null);
        variationOptionRepository.delete(existOrNotVariationOption);
    }

    public VariationOption createVariationOption(Long variationId, CreateVariationOptionRequest createVariationOptionRequest){
            Variation variation = variationRepository.findById(variationId).orElseThrow();

            String variationOptionValue = createVariationOptionRequest.getValue();
            VariationOption variationOption = variationOptionRepository.findByValue(variationOptionValue)
                    .orElse(VariationOption
                            .builder()
                            .value(variationOptionValue)
                            .variation(variation)
                            .build());
            if(variationOption.getId() == null){
               return variationOptionRepository.save(variationOption);
            }
            throw new RuntimeException("already exist variation option: " + variationOptionValue);
    }

    public VariationOption updateVariationOption(Long variationOptionId,UpdateVariationOptionRequest updateVariationOptionRequest){
        VariationOption variationOption = variationOptionRepository.findById(variationOptionId).orElseThrow();
        variationOption.setValue(updateVariationOptionRequest.getValue());

        Long variationId = updateVariationOptionRequest.getVariationId();
        if(variationId != null){
            Variation variation = variationRepository.findById(variationId).orElseThrow(()-> new RuntimeException("Not found variation id " + variationId));
            variationOption.setVariation(variation);
        }
        return variationOptionRepository.save(variationOption);
    }
}
