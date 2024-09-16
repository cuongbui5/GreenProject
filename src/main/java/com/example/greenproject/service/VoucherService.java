package com.example.greenproject.service;

import com.example.greenproject.dto.req.VoucherRequest;
import com.example.greenproject.dto.req.UpdateVoucherRequest;
import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.dto.res.VoucherDto;
import com.example.greenproject.exception.NotFoundException;
import com.example.greenproject.model.Voucher;
import com.example.greenproject.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherRepository voucherRepository;

    public Object getAllVouchers(Integer pageNum, Integer pageSize,String search){

        if(pageNum==null || pageSize==null){

            return getAllVouchersList();
        }

        Pageable pageable = PageRequest.of(pageNum,pageSize);
        Page<Voucher> vouchers;
        if(search!=null){
            vouchers= searchVoucher(search,pageable);
        }else {
            vouchers = voucherRepository.findAll(pageable);
        }

        List<VoucherDto> voucherDtos=vouchers.getContent().stream().map(Voucher::mapToVoucherDto).toList();
        return new PaginatedResponse<>(
                voucherDtos,
                vouchers.getTotalPages(),
                vouchers.getNumber(),
                vouchers.getTotalElements()
        );
    }

    private List<VoucherDto> getAllVouchersList(){
        return voucherRepository.findAll().stream().map(Voucher::mapToVoucherDto).toList();
    }

    private Page<Voucher> searchVoucher(String search,Pageable pageable){
        return voucherRepository.findByNameContainingIgnoreCase(search,pageable);
    }


    public VoucherDto createVoucher(VoucherRequest voucherRequest){
        if(voucherRequest.getEndDate().isBefore(voucherRequest.getStartDate()) ||
           voucherRequest.getEndDate().isBefore(ZonedDateTime.now())){
            throw new RuntimeException("Ngay het han khong hop le!");
        }
        Voucher voucher = new Voucher();
        return getVoucherDto(voucherRequest, voucher);

    }

    public VoucherDto updateVoucher(Long voucherId, VoucherRequest voucherRequest){
        Optional<Voucher> voucherOptional=voucherRepository.findById(voucherId);
        if(voucherOptional.isEmpty()){
            throw new NotFoundException("Voucher not found");
        }

        Voucher voucher=voucherOptional.get();
        return getVoucherDto(voucherRequest, voucher);
    }

    private VoucherDto getVoucherDto(VoucherRequest voucherRequest, Voucher voucher) {
        voucher.setName(voucherRequest.getName());
        voucher.setDescription(voucherRequest.getDescription());
        voucher.setQuantity(voucherRequest.getQuantity());
        voucher.setPointsRequired(voucherRequest.getPointsRequired());
        voucher.setType(voucherRequest.getType());
        voucher.setValue(voucherRequest.getValue());
        voucher.setStartDate(voucherRequest.getStartDate());
        voucher.setEndDate(voucherRequest.getEndDate());
        return voucherRepository.save(voucher).mapToVoucherDto();
    }


    public void deleteVoucher(Long voucherId){
       voucherRepository.deleteById(voucherId);
    }




}
