package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateVoucherRequest;
import com.example.greenproject.dto.req.FilteringVoucherRequest;
import com.example.greenproject.dto.req.UpdateVoucherRequest;
import com.example.greenproject.dto.req_abstract.AbstractVoucherRequest;
import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.model.Voucher;
import com.example.greenproject.model.enums.VoucherType;
import com.example.greenproject.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherRepository voucherRepository;

    public PaginatedResponse<Voucher> getAllVouchers(int pageNum, int pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        Page<Voucher> vouchers = voucherRepository.findAll(pageable);
        return new PaginatedResponse<>(
                vouchers.getContent(),
                vouchers.getTotalPages(),
                vouchers.getNumber(),
                vouchers.getTotalElements()
        );
    }

    public PaginatedResponse<Voucher> getAllVoucherAvailable(int pageNum, int pageSize){
        List<Voucher> vouchersList = voucherRepository.findAll();
        Pageable pageable = PageRequest.of(pageNum,pageSize);

        //TODO: sẽ cho vouchers hiển thị nếu isActive = true và startDate <= Date.now() <= endDate
        List<Voucher> filterVouchers = vouchersList.stream()
                .filter(Voucher::getIsActive)
                .filter(voucher -> voucher.getStartDate().isBefore(LocalDateTime.now())
                        && voucher.getEndDate().isAfter(LocalDateTime.now()))
                .toList();

        Page<Voucher> voucherPage = new PageImpl<>(filterVouchers,pageable,filterVouchers.size());

        return new PaginatedResponse<>(
                voucherPage.getContent(),
                voucherPage.getTotalPages(),
                voucherPage.getNumber(),
                voucherPage.getTotalElements()
        );
    }

    public Voucher createVoucher(CreateVoucherRequest createVoucherRequest){
        Voucher createVoucher = helpingSetVoucherData(createVoucherRequest);

        if(createVoucher.getEndDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Tạo voucher thất bại vì voucher đã quá hạn");
        }

        List<Voucher> vouchers = voucherRepository.findByName(createVoucher.getName());
        if(vouchers.isEmpty()){
            return voucherRepository.save(createVoucher);
        }else{
            //TODO: kiểm tra xem voucher đã hết hạn chưa ( isActive ) => true -> throw đã tồn tại ; false -> cho tạo
            //TODO: kiểm tra xem voucher cần tạo có type hoặc value đã tồn tại chưa
            Optional<Voucher> existVoucherOptional = vouchers.stream()
                    .filter(voucher -> voucher.getType().equals(createVoucher.getType()))
                    .filter(voucher -> voucher.getValue().equals(createVoucher.getValue()))
                    .findFirst();

            //TODO: Nếu không tìm thấy thì thêm
            if(existVoucherOptional.isEmpty()) {
                //TODO: tạo mới voucher
                return voucherRepository.save(createVoucher);
            }else{
                //TODO: Nếu tìm thấy thì thêm -> kiểm tra xem voucher đã hết hạn chưa
                Voucher existVoucher = existVoucherOptional.get();
                if(!existVoucher.getIsActive()){
                    return voucherRepository.save(createVoucher);
                }
                throw new RuntimeException("Voucher đã tồn tại rồi");
            }
        }
    }


    public Voucher updateVoucher(Long voucherId, UpdateVoucherRequest updateVoucherRequest){
        if(voucherId == null){
            throw new RuntimeException("Thiếu voucher id trong request");
        }
        Voucher voucher = voucherRepository.findById(voucherId).orElseThrow(()-> new RuntimeException("Không tìm thấy voucher id " + voucherId));

        //TODO: tạo 1 abstract class cho createRequest và updateRequest
        helpingSetVoucherData(voucher,updateVoucherRequest);
        return voucherRepository.save(voucher);
    }


    public void deleteVoucher(Long voucherId){
        Voucher voucher = voucherRepository.findById(voucherId).orElseThrow(()-> new RuntimeException("Không tìm thấy voucher id " + voucherId));
        voucherRepository.delete(voucher);
    }

    public PaginatedResponse<Voucher> filteringVoucher(int pageSize, int pageNum,FilteringVoucherRequest filteringVoucherRequest){
        String name = filteringVoucherRequest.getName();
        VoucherType type = filteringVoucherRequest.getType();
        Boolean isActive = filteringVoucherRequest.getIsActive();
        Pageable pageable = PageRequest.of(pageSize,pageNum);

        List<Voucher> vouchers = voucherRepository.findAll();
        List<Voucher> filteringVoucher = vouchers.stream()
                .filter(voucher -> voucher.getName() == null || voucher.getName().contains(name))
                .filter(voucher -> type == null || voucher.getType().equals(type))
                .filter(voucher -> isActive == null || voucher.getIsActive().equals(isActive))
                .toList();

        Page<Voucher> voucherPage = new PageImpl<>(filteringVoucher,pageable,filteringVoucher.size());

        return new PaginatedResponse<>(
                voucherPage.getContent(),
                voucherPage.getTotalPages(),
                voucherPage.getNumber(),
                voucherPage.getTotalElements()
        );
    }

    private Voucher helpingSetVoucherData(AbstractVoucherRequest request){
        return helpingSetVoucherData(new Voucher(),request);
    }

    private Voucher helpingSetVoucherData(Voucher voucher, AbstractVoucherRequest request){
        checkNullAndSetData(request.getName(), voucher::setName);
        checkNullAndSetData(request.getDescription(),voucher::setDescription);
        checkNullAndSetData(request.getQuantity(),voucher::setQuantity);
        checkNullAndSetData(request.getPointsRequired(),voucher::setPointsRequired);
        checkNullAndSetData(request.getVersion(),voucher::setVersion);
        checkNullAndSetData(request.getType(),voucher::setType);
        checkNullAndSetData(request.getValue(),voucher::setValue);
        checkNullAndSetData(request.getStartDate(),voucher::setStartDate);
        checkNullAndSetData(request.getEndDate(),(endDate)->{
            voucher.setEndDate(endDate);
            boolean isActive = endDate.isAfter(LocalDateTime.now());
            voucher.setIsActive(isActive);
        });
        return voucher;
    }

    private <T> void checkNullAndSetData(T data, Consumer<T> consumer){
        if(data != null) {
            consumer.accept(data);
        }
    }
}
