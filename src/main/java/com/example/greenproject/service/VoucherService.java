package com.example.greenproject.service;

import com.example.greenproject.dto.req.VoucherRequest;
import com.example.greenproject.dto.req.UpdateVoucherRequest;
import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.dto.res.VoucherDto;
import com.example.greenproject.exception.NotFoundException;
import com.example.greenproject.model.User;
import com.example.greenproject.model.Voucher;
import com.example.greenproject.model.enums.VoucherType;
import com.example.greenproject.repository.UserRepository;
import com.example.greenproject.repository.VoucherRepository;
import com.example.greenproject.security.UserInfo;
import com.example.greenproject.utils.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherRepository voucherRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public Object getAllVouchers(Integer pageNum, Integer pageSize,String search){
        System.out.println("getAllVouchers");
        if(pageNum==null || pageSize==null){

            return getAllVouchersList();
        }

        Pageable pageable = PageRequest.of(pageNum-1,pageSize);
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
                vouchers.getNumber()+1,
                vouchers.getTotalElements()
        );
    }

    //------------------------------------------------------------------------------------------------------------------------------
    public Object getValidVouchers(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum-1,pageSize);
        ZonedDateTime now = ZonedDateTime.now();


        Page<Voucher> validVouchers = voucherRepository.findValidVouchers(now, pageable);

        return new PaginatedResponse<>(
                validVouchers.getContent().stream().map(Voucher::mapToVoucherDto).toList(),
                validVouchers.getTotalPages(),
                validVouchers.getNumber()+1,
                validVouchers.getTotalElements()
        );
    }
    //------------------------------------------------------------------------------------------------------------------------------

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
        VoucherType voucherType=voucherRequest.getType();
        if(voucherType==VoucherType.DISCOUNT_PERCENTAGE&&voucherRequest.getValue()>=100){
            throw new RuntimeException("Value ko hop le!");

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


    @Transactional
    public Object getVouchersByUserId(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum-1,pageSize);

        UserInfo userInfo= Utils.getUserInfoFromContext();

        Page<Voucher> vouchers = voucherRepository.findAllByUserId(userInfo.getId(),pageable);

        return new PaginatedResponse<>(
                vouchers.getContent().stream().map(Voucher::mapToVoucherDto).toList(),
                vouchers.getTotalPages(),
                vouchers.getNumber()+1,
                vouchers.getTotalElements()
        );
    }

    @Transactional
    public VoucherDto redeemVoucher(Long voucherId) {
        User user = userService.getUserByUserInfo();

        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy voucher id : " + voucherId));


        if (user.getPoints() < voucher.getPointsRequired()) {
            throw new RuntimeException("Không đủ điểm để đổi voucher");
        }

        if (voucher.getQuantity() <= 0) {
            throw new RuntimeException("Đã hết voucher");
        }

        if(user.getVouchers().stream().anyMatch((v)->v.getName().equals(voucher.getName()))){
            throw new RuntimeException("Bạn đã sở hữu voucher này rồi !");
        }

        user.setPoints(user.getPoints() - voucher.getPointsRequired());
        voucher.setQuantity(voucher.getQuantity() - 1);
        user.getVouchers().add(voucher);
        userService.updateUserPoint(user);

        return voucher.mapToVoucherDto();
    }

    public void deleteVoucherAfterPaymentSuccess(Long voucherId,Long userId) {
         voucherRepository.deleteUserVoucher(userId,voucherId);

    }

}
