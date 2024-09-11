package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateVariationOptionRequest;
import com.example.greenproject.dto.req.CreateVariationRequest;
import com.example.greenproject.dto.req.UpdateVariationRequest;
import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.dto.res.VariationDto;
import com.example.greenproject.exception.NotFoundException;
import com.example.greenproject.model.Category;
import com.example.greenproject.model.Variation;
import com.example.greenproject.model.VariationOption;
import com.example.greenproject.repository.CategoryRepository;
import com.example.greenproject.repository.VariationOptionRepository;
import com.example.greenproject.repository.VariationRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VariationService {
    private final VariationRepository variationRepository;
    private final CategoryRepository categoryRepository;

    public Object getAllVariation(Integer pageNum,Integer pageSize){
        if(pageNum == null || pageSize ==null){
            return variationRepository.findAll().stream().map(Variation::mapToVariationDto).toList();
        }
        Page<Variation> variationPage=variationRepository.findAll(PageRequest.of(pageNum,pageSize));
        List<VariationDto> data=variationPage.getContent().stream().map(Variation::mapToVariationDto).toList();
        return new PaginatedResponse<>(
                data,
                variationPage.getTotalPages(),
                variationPage.getNumber()+1,
                variationPage.getTotalElements()
        );




    }


    public Variation createVariation(CreateVariationRequest createVariationRequest){
        String variationName = createVariationRequest.getName();
        Optional<Category> category=categoryRepository.findById(createVariationRequest.getCategoryId());
        if(category.isEmpty()){
            throw new NotFoundException("Không tìm thấy danh mục có id :"+createVariationRequest.getCategoryId());
        }
        Optional<Variation> variation = variationRepository.findByName(variationName);
        if(variation.isPresent()){
            throw new RuntimeException("Biến thể đã tồn tại!");
        }else {
            HashSet<Category> categories = new HashSet<>();
            categories.add(category.get());
            Variation newVariation = new Variation();
            newVariation.setName(createVariationRequest.getName());
            newVariation.setCategories(categories);
            return variationRepository.save(newVariation);
        }


    }


    public Variation updateVariationById(Long variationId, UpdateVariationRequest updateVariationRequest){
        Optional<Variation> variationOptional = variationRepository.findById(variationId);
        if(variationOptional.isEmpty()){
            throw new NotFoundException("Không tìm thấy biến thể với id: "+variationId);
        }
        Variation variation=variationOptional.get();
        System.out.println("name:"+updateVariationRequest.getName());
        variation.setName(updateVariationRequest.getName());
        return variationRepository.save(variation);
    }


    public List<VariationDto> getAllVariationByCategoryId(Long categoryId) {
        return variationRepository.getAllVariationByCategoryId(categoryId).stream().map(Variation::mapToVariationDto).toList();
    }

    public void deleteVariation(Long id) {
         variationRepository.deleteById(id);
    }
}
