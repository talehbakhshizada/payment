package com.ms.payment.mapper;

import com.ms.payment.dao.entity.PaymentEntity;
import com.ms.payment.model.request.PaymentRequestDto;
import com.ms.payment.model.response.PaymentResponseDto;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class PaymentMapper {

    public static PaymentEntity mapRequestToEntity(PaymentRequestDto request){
        return PaymentEntity.builder()
                .amount(request.getAmount())
                .description(request.getDescription())
                .build();
    }

    public static PaymentResponseDto mapEntityToResponse(PaymentEntity entity) {
        return PaymentResponseDto.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .description(entity.getDescription())
                .responseAt(LocalDateTime.now())
                .build();
    }


    public static void updatePayment(PaymentRequestDto request, PaymentEntity entity) {
        entity.setAmount(request.getAmount());
        entity.setDescription(request.getDescription());
    }

}
