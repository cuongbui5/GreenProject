package com.example.greenproject.dto.req_abstract;

import com.example.greenproject.model.enums.VoucherType;

import java.time.LocalDateTime;

public interface VoucherRequestInterface {
     String getName();

     void setName(String name);

     String getDescription();

     void setDescription(String description);

     Integer getQuantity();

     void setQuantity(Integer quantity);

     Integer getPointsRequired();

     void setPointsRequired(Integer pointsRequired);

     Long getVersion();

     void setVersion(Long version) ;

     VoucherType getType() ;

     void setType(VoucherType type) ;

     double getValue() ;

     void setValue(double value) ;

     LocalDateTime getStartDate();

     void setStartDate(LocalDateTime startDate) ;

     LocalDateTime getEndDate() ;

     void setEndDate(LocalDateTime endDate) ;
}