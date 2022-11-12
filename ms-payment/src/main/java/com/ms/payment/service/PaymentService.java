package com.ms.payment.service;

import com.ms.payment.client.CountryClient;
import com.ms.payment.dao.entity.PaymentEntity;
import com.ms.payment.dao.repository.PaymentRepository;
import com.ms.payment.exception.NotFoundException;
import com.ms.payment.mapper.PaymentMapper;
import com.ms.payment.model.request.PaymentCriteria;
import com.ms.payment.model.request.PaymentRequestDto;
import com.ms.payment.model.response.PageablePaymentResponse;
import com.ms.payment.model.response.PaymentResponseDto;
import com.ms.payment.service.specification.PaymentSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.ms.payment.mapper.PaymentMapper.mapEntityToResponse;
import static com.ms.payment.mapper.PaymentMapper.mapRequestToEntity;
import static com.ms.payment.model.constant.ExceptionConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PaymentService {
    PaymentRepository paymentRepository;
    CountryClient countryClient;

    public void savePayment(PaymentRequestDto request) {
        log.info("ActionLog.savePayment.started currency:{}", request.getCurrency());
        countryClient.getAvailableCountries(request.getCurrency())
                .stream()
                .filter(country -> country.getRemainingLimit().compareTo(request.getAmount()) > 0)
                .findFirst()
                .orElseThrow(() ->
                        new NotFoundException(String.format(COUNTRY_NOT_FOUND_MESSAGE,
                                request.getAmount(), request.getCurrency()),
                                COUNTRY_NOT_FOUND_CODE));
        paymentRepository.save(mapRequestToEntity(request));
        log.info("ActionLog.savePayment.success currency:{}", request.getCurrency());
    }

    public PaymentResponseDto getPaymentById(Long id) {
        log.info("ActionLog.getPayment.start id:{}", id);
        var payment = fetchPaymentIfExist(id);
        log.info("ActionLog.getPayment.success id:{}", id);
        return mapEntityToResponse(payment);
    }

    public PageablePaymentResponse getAllPayments(int page, int count, PaymentCriteria paymentCriteria) {
        log.info("ActionLog.getAllPayments.start");
        var pageable = PageRequest.of(page, count, Sort.by(Direction.DESC, "id"));
        var pageablePayments = paymentRepository
                .findAll(new PaymentSpecification(paymentCriteria), pageable);

        var payments = pageablePayments.getContent()
                .stream()
                .map(PaymentMapper::mapEntityToResponse)
                .collect(Collectors.toList());

        log.info("ActionLog.getAllPayments.success");
        return PageablePaymentResponse.builder()
                .payments(payments)
                .totalPages(pageablePayments.getTotalPages())
                .totalElements(pageablePayments.getTotalElements())
                .hasNextPage(pageablePayments.hasNext())
                .build();
    }

    public void updatePayment(Long id, PaymentRequestDto request) {
        log.info("ActionLog.updatePayment.start id:{}", id);
        var payment = fetchPaymentIfExist(id);
        PaymentMapper.updatePayment(request, payment);
        paymentRepository.save(payment);
        log.info("ActionLog.updatePayment.success id:{}", id);
    }

    public void deletePayment(Long id) {
        log.info("ActionLog.deletePayment.start id:{}", id);
        paymentRepository.deleteById(id);
        log.info("ActionLog.deletePayment.success id:{}", id);
    }

    private PaymentEntity fetchPaymentIfExist(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }
}
