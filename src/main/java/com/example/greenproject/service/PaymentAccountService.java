package com.example.greenproject.service;

import com.example.greenproject.dto.req.LinkPaymentAccount;
import com.example.greenproject.model.PaymentAccount;
import com.example.greenproject.model.User;
import com.example.greenproject.repository.PaymentAccountRepository;
import com.example.greenproject.security.UserInfo;
import com.example.greenproject.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentAccountService {
    private final PaymentAccountRepository paymentAccountRepository;
    private final UserService userService;
    public List<PaymentAccount> findAllByUserId(Long userId) {
        return paymentAccountRepository.findByUserId(userId);
    }

    public PaymentAccount linkUserToPaymentAccount(LinkPaymentAccount linkPaymentAccount) {
        Optional<PaymentAccount> paymentAccountOptional=paymentAccountRepository
                .findByAccountNumberAndBankId(linkPaymentAccount.getAccountNumber(), linkPaymentAccount.getBankId());

        if(paymentAccountOptional.isEmpty()){
            throw new RuntimeException("Tài khoản thanh toán không tồn tại!");
        }
        PaymentAccount paymentAccount=paymentAccountOptional.get();
        if(paymentAccount.getUser()!=null){
            throw new RuntimeException("Tài khoản thanh toán này đã được liên kết rồi!");
        }
        if(!Objects.equals(paymentAccount.getFullName(), linkPaymentAccount.getFullName())
                ||!Objects.equals(paymentAccount.getPinCode(), linkPaymentAccount.getPinCode())){
            throw new RuntimeException("Thông tin tài khoản thanh toán không đúng!Liên kết thất bại");
        }

        User user=userService.getUserByUserInfo();
        paymentAccount.setUser(user);
        return paymentAccountRepository.save(paymentAccount);

    }

    public List<PaymentAccount> findAllByUser() {
        UserInfo userInfo= Utils.getUserInfoFromContext();
        return findAllByUserId(userInfo.getId());
    }
}
