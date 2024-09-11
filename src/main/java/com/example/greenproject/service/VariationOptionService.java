package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateVariationOptionRequest;
import com.example.greenproject.dto.req.UpdateVariationOptionRequest;
import com.example.greenproject.exception.NotFoundException;
import com.example.greenproject.model.Variation;
import com.example.greenproject.model.VariationOption;
import com.example.greenproject.repository.VariationOptionRepository;
import com.example.greenproject.repository.VariationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VariationOptionService {
    private final VariationOptionRepository variationOptionRepository;
    private final VariationRepository variationRepository;

    public void createVariationOption(CreateVariationOptionRequest createVariationOptionRequest) {
        if(createVariationOptionRequest.getVariationId()!=null){
            Optional<Variation> variation = variationRepository.findById(createVariationOptionRequest.getVariationId());
            if(variation.isEmpty()){
                throw new NotFoundException("Không tìm thấy biến thể với id: "+createVariationOptionRequest.getVariationId());
            }


            String values=createVariationOptionRequest.getValues();
            if(values.contains(",")){
                String[] valueArray = values.split(",");
                for (String s : valueArray) {
                    if(variationOptionRepository.findByValue(s).isPresent()){
                        throw new RuntimeException("Đã có value rồi");
                    }
                    VariationOption variationOption = new VariationOption();
                    variationOption.setValue(s);
                    variationOption.setVariation(variation.get());
                    variationOptionRepository.save(variationOption);
                }

            }else {
                VariationOption variationOption = new VariationOption();
                variationOption.setValue(values);
                variationOption.setVariation(variation.get());
                variationOptionRepository.save(variationOption);
            }

        }else {
            throw new RuntimeException("Không thấy biến thể id!");
        }


    }

    public void deleteVariation(Long id){
        variationOptionRepository.deleteById(id);
    }

    public VariationOption updateVariationOption(Long id, UpdateVariationOptionRequest updateVariationOptionRequest){
        VariationOption existOrNotVariation = variationOptionRepository.findById(id).orElseThrow();
        existOrNotVariation.setValue(updateVariationOptionRequest.getValue());
        return variationOptionRepository.save(existOrNotVariation);
    }
}
