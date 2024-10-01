package com.example.greenproject.service;

import com.example.greenproject.dto.res.PaginatedResponse;
import com.example.greenproject.dto.res.VoucherDto;
import com.example.greenproject.model.User;
import com.example.greenproject.model.UserVoucher;
import com.example.greenproject.model.Voucher;
import com.example.greenproject.model.pk.UserVoucherId;
import com.example.greenproject.repository.VoucherRepository;
import com.example.greenproject.repository.VoucherUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserVoucherService {

    private final VoucherUserRepository userVoucherRepository;
    private final VoucherRepository voucherRepository;
    private final UserService userService;

    @Transactional
    public Object getVouchersByUserId(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum-1,pageSize);

        User user = userService.getUserByUserInfo();

        Page<UserVoucher> userVouchers = userVoucherRepository.findByIdUserId(user.getId(),pageable);

        return new PaginatedResponse<>(
                userVouchers.getContent().stream().map((userVoucher -> userVoucher.getId().getVoucher().mapToVoucherDto())).toList(),
                userVouchers.getTotalPages(),
                userVouchers.getNumber()+1,
                userVouchers.getTotalElements()
        );
    }

    public VoucherDto redeemVoucher(Long voucherId) {
        User user = userService.getUserByUserInfo();

        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy voucher id : " + voucherId));

        // Check if the user has enough points to redeem the voucher
        if (user.getPoints() < voucher.getPointsRequired()) {
            throw new RuntimeException("Không đủ điểm để đổi voucher");
        }

        if (voucher.getQuantity() <= 0) {
            throw new RuntimeException("Đã hết voucher");
        }

        user.setPoints(user.getPoints() - voucher.getPointsRequired());
        User updateUser = userService.updateUserPoint(user);

        UserVoucherId userVoucherId = new UserVoucherId();
        userVoucherId.setUser(updateUser);
        userVoucherId.setVoucher(voucher);

        Optional<UserVoucher> userVoucherOptional = userVoucherRepository.findById(userVoucherId);
        if(userVoucherOptional.isPresent()){
            UserVoucher userVoucher = userVoucherOptional.get();
            userVoucher.setQuantity(userVoucher.getQuantity() + 1);
            userVoucherRepository.save(userVoucher);
        }else{
            UserVoucher userVoucher = new UserVoucher(userVoucherId, 1);
            userVoucherRepository.save(userVoucher);
        }
        voucher.setQuantity(voucher.getQuantity() - 1);
        voucherRepository.save(voucher);

        return voucher.mapToVoucherDto();
    }
}
