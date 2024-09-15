package com.example.greenproject.service;

import com.example.greenproject.dto.req.CreateVoucherRequest;
import com.example.greenproject.dto.req.UpdateVoucherRequest;
import com.example.greenproject.dto.req_abstract.VoucherRequestInterface;
import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.model.Voucher;
import com.example.greenproject.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
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
        return new PaginatedResponse<>(
                vouchers.getContent(),
                vouchers.getTotalPages(),
                vouchers.getNumber(),
                vouchers.getTotalElements()
        );
    }

    private List<Voucher> getAllVouchersList(){
        return voucherRepository.findAll();
    }

    private Page<Voucher> searchVoucher(String search,Pageable pageable){
        return voucherRepository.findByNameContainingIgnoreCase(search,pageable);
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

    private Voucher helpingSetVoucherData(VoucherRequestInterface request){
        return helpingSetVoucherData(new Voucher(),request);
    }

    private Voucher helpingSetVoucherData(Voucher voucher, VoucherRequestInterface request){
        voucher.setName(request.getName());
        voucher.setDescription(request.getDescription());
        voucher.setQuantity(request.getQuantity());
        voucher.setPointsRequired(request.getPointsRequired());
        voucher.setVersion(request.getVersion());
        voucher.setType(request.getType());
        voucher.setValue(request.getValue());
        voucher.setStartDate(request.getStartDate());
        voucher.setEndDate(request.getEndDate());

        boolean isActive = request.getEndDate().isAfter(LocalDateTime.now());
        voucher.setIsActive(isActive);
        return voucher;
    }

}
