package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateVariationOptionRequest;
import com.example.greenproject.dto.req.CreateVariationRequest;
import com.example.greenproject.dto.req.UpdateVariationRequest;
import com.example.greenproject.exception.category_exception.CategoryNotFoundException;
import com.example.greenproject.model.Category;
import com.example.greenproject.model.Variation;
import com.example.greenproject.model.VariationOption;
import com.example.greenproject.repository.CategoryRepository;
import com.example.greenproject.repository.VariationOptionRepository;
import com.example.greenproject.repository.VariationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

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
        Long categoryId = createVariationRequest.getCategoryId();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new CategoryNotFoundException("Not found category id " + categoryId));

        Variation variation = variationRepository.findByName(variationName)
                .orElse(Variation
                        .builder()
                        .name(variationName)
                        .build());
        if(variation.getId() == null){
            variation.addCategory(category);
            return variationRepository.save(variation);
        }
        throw new RuntimeException("Already exist variation");
    }


    public Variation updateVariation(Long variationId, UpdateVariationRequest updateVariationRequest){
        Variation variation = variationRepository.findById(variationId).orElseThrow();
        variation.setName(updateVariationRequest.getName());
        return variationRepository.save(variation);
    }

    public Variation addCategoryToVariation(Long variationId, Long categoryId){
        Variation variation = variationRepository.findById(variationId).orElseThrow();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new CategoryNotFoundException("Not found category id " + categoryId));
        variation.addCategory(category);
        return variationRepository.save(variation);
    }

    public void deleteVariation(Long id){
        Variation variation = variationRepository.findById(id).orElseThrow();
        variation.setCategories(null);
        variationRepository.delete(variation);
    }

    public void removeCategoryFromVariation(Long variationId, Long categoryId){
        Variation variation = variationRepository.findById(variationId).orElseThrow();
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        variation.removeCategory(category);
        variationRepository.save(variation);
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
