package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateVariationRequest;
import com.example.greenproject.dto.req.UpdateVariationRequest;
import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.dto.res.VariationDto;
import com.example.greenproject.dto.res.VariationDtoWithOptions;
import com.example.greenproject.exception.NotFoundException;
import com.example.greenproject.model.Category;
import com.example.greenproject.model.Variation;
import com.example.greenproject.repository.CategoryRepository;
import com.example.greenproject.repository.VariationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VariationService {
    private final VariationRepository variationRepository;
    private final CategoryRepository categoryRepository;

    public Object getAllVariations(Integer pageNum, Integer pageSize, String search, Long categoryId){
        if(pageNum == null || pageSize ==null){
            return variationRepository.findAll().stream().map(Variation::mapToVariationDto).toList();
        }
        Pageable pageable = PageRequest.of(pageNum-1, pageSize);
        Page<Variation> variations = null;
        if(search==null&&categoryId==null){
            variations = variationRepository.findAll(pageable);
        }
        
        if(categoryId != null){
            variations = variationRepository.findAllByCategoryId(categoryId, pageable);
        }
        
        if(search!=null){
            variations = searchVariation(search,pageable);
        }

        List<VariationDto> variationDtos = variations.getContent().stream().map(Variation::mapToVariationDto).toList();
        return new PaginatedResponse<>(
                variationDtos,
                variations.getTotalPages(),
                variations.getNumber()+1,
                variations.getTotalElements()
        );




    }

    private Page<Variation> searchVariation(String search, Pageable pageable) {
        return variationRepository.findByNameContainingIgnoreCase(search,pageable);
    }


    public VariationDto createVariation(CreateVariationRequest createVariationRequest){
        Category category=null;
        if(createVariationRequest.getCategoryId()!=null){
            category = categoryRepository
                    .findById(createVariationRequest.getCategoryId())
                    .orElseThrow(()->new NotFoundException("Category not found"));
        }

        Variation newVariation = new Variation();
        newVariation.setName(createVariationRequest.getName());
        newVariation.setCategory(category);
        return variationRepository.save(newVariation).mapToVariationDto();


    }


    public VariationDto updateVariationById(Long variationId, UpdateVariationRequest updateVariationRequest) {
        Optional<Variation> variationOptional = variationRepository.findById(variationId);
        if (variationOptional.isEmpty()) {
            throw new NotFoundException("Không tìm thấy biến thể với id: " + variationId);
        }

        Variation variation = variationOptional.get();

        if (variation.getCategory() == null || !variation.getCategory().getId().equals(updateVariationRequest.getCategoryId())) {
            Optional<Category> category = categoryRepository.findById(updateVariationRequest.getCategoryId());
            if (category.isEmpty()) {
                throw new NotFoundException("Không tìm thấy danh muc với id: " + updateVariationRequest.getCategoryId());
            }
            variation.setCategory(category.get());
        }
        variation.setName(updateVariationRequest.getName());

        return variationRepository.save(variation).mapToVariationDto();
    }





    public List<VariationDtoWithOptions> getAllVariationByCategoryId(Long categoryId) {
        List<VariationDtoWithOptions> variationDtoWithOptions = new ArrayList<>();
        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        if (category.getParent() != null) {
            List<VariationDtoWithOptions> parentVariations = getAllVariationByCategoryId(category.getParent().getId());
            variationDtoWithOptions.addAll(parentVariations);
        }
        List<VariationDtoWithOptions> currentVariations = variationRepository.findAllByCategoryId(categoryId)
                .stream()
                .map(Variation::mapToVariationDtoWithOptions)
                .toList();

        variationDtoWithOptions.addAll(currentVariations);

        return variationDtoWithOptions;
    }


    public void deleteVariation(Long id) {
         variationRepository.deleteById(id);
    }
}
