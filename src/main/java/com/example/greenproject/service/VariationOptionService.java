package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateVariationOptionRequest;
import com.example.greenproject.dto.req.UpdateVariationOptionRequest;
import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.dto.res.VariationOptionDto;
import com.example.greenproject.exception.NotFoundException;
import com.example.greenproject.model.Variation;
import com.example.greenproject.model.VariationOption;
import com.example.greenproject.repository.VariationOptionRepository;
import com.example.greenproject.repository.VariationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VariationOptionService {
    private final VariationOptionRepository variationOptionRepository;
    private final VariationRepository variationRepository;

    public void createVariationOption(CreateVariationOptionRequest createVariationOptionRequest) {
        Variation variation=null;
        if(createVariationOptionRequest.getVariationId()!=null){
            variation=variationRepository
                    .findById(createVariationOptionRequest.getVariationId())
                    .orElseThrow(()->new NotFoundException("Không tìm thấy biến thể với id!"));
        }

        String values=createVariationOptionRequest.getValue();
        if(values.contains(",")){
            String[] valueArray = values.split(",");
            for (String s : valueArray) {
                VariationOption variationOption = new VariationOption();
                variationOption.setValue(s);
                variationOption.setVariation(variation);
                variationOptionRepository.save(variationOption);
            }

        }else {
            VariationOption variationOption = new VariationOption();
            variationOption.setValue(values);
            variationOption.setVariation(variation);
            variationOptionRepository.save(variationOption);
        }

    }

    public void deleteVariation(Long id){
        variationOptionRepository.deleteById(id);
    }



    public Object getAllVariationOptions(Integer pageNum, Integer pageSize, String search, Long variationId) {
        if(pageNum == null || pageSize ==null){
            return variationOptionRepository.findAll().stream().map(VariationOption::mapToVariationOptionDto).toList();
        }
        Pageable pageable = PageRequest.of(pageNum-1, pageSize);
        Page<VariationOption> variationOptions = null;
        if(variationId==null&&search==null){
            variationOptions = variationOptionRepository.findAll(pageable);
        }
        
        if(search!=null){
            variationOptions = searchVariationOption(search,pageable);
        }
        
        if(variationId!=null){
            variationOptions = variationOptionRepository.findByVariationId(variationId,pageable);
        }

        List<VariationOptionDto> variationOptionDtos = variationOptions.getContent().stream().map(VariationOption::mapToVariationOptionDto).toList();
        return new PaginatedResponse<>(
                variationOptionDtos,
                variationOptions.getTotalPages(),
                variationOptions.getNumber()+1,
                variationOptions.getTotalElements()
        );
    }

    private Page<VariationOption> searchVariationOption(String search, Pageable pageable) {
        return variationOptionRepository.findByValueContainingIgnoreCase(search,pageable);
    }

    public Object updateVariationOptionById(Long variationOptionId, UpdateVariationOptionRequest updateVariationOptionRequest) {
        Optional<VariationOption> variationOptionOptional = variationOptionRepository.findById(variationOptionId);
        if (variationOptionOptional.isEmpty()) {
            throw new NotFoundException("Không tìm thấy tuy chon với id: " + variationOptionId);
        }

        VariationOption variationOption = variationOptionOptional.get();




        if (variationOption.getVariation() == null || !variationOption.getVariation().getId().equals(updateVariationOptionRequest.getVariationId())) {
            Optional<Variation> variation = variationRepository.findById(updateVariationOptionRequest.getVariationId());
            if (variation.isEmpty()) {
                throw new NotFoundException("Không tìm thấy bien the với id: " + updateVariationOptionRequest.getVariationId());
            }
            variationOption.setVariation(variation.get());
        }
        variationOption.setValue(updateVariationOptionRequest.getValue());

        return variationOptionRepository.save(variationOption).mapToVariationOptionDto();
    }
}
