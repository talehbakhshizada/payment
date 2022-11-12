package com.ms.payment.controller;

import com.ms.payment.model.request.PaymentCriteria;
import com.ms.payment.model.request.PaymentRequestDto;
import com.ms.payment.model.response.PageablePaymentResponse;
import com.ms.payment.model.response.PaymentResponseDto;
import com.ms.payment.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/payments")
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class PaymentController {
    PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void savePayment(@RequestBody PaymentRequestDto request) {
        paymentService.savePayment(request);
    }

    @GetMapping("/{id}")
    public PaymentResponseDto getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @GetMapping
    public PageablePaymentResponse getAllPayments(@RequestParam int page,
                                                        @RequestParam int count,
                                                        PaymentCriteria paymentCriteria) {
        return paymentService.getAllPayments(page,count,paymentCriteria);
    }

    @PutMapping("/{id}")
    public void updatePayment(@PathVariable Long id,@RequestBody PaymentRequestDto request){
        paymentService.updatePayment(id,request);
    }

    @DeleteMapping("/{id}")
    public void deletePayment(@PathVariable Long id){
        paymentService.deletePayment(id);
    }
}
