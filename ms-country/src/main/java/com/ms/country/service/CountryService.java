package com.ms.country.service;

import com.ms.country.model.CountryResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CountryService {

    public List<CountryResponseDto> getAvailableCountries(String currency) {
        log.info("ActionLog.getAvailableCountries.started currency:{}",currency);
        if (currency.equals("USD")) {
            return List.of(new CountryResponseDto("UK", BigDecimal.valueOf(100000)),
                    new CountryResponseDto("Germany", BigDecimal.TEN));
        }
        log.info("ActionLog.getAvailableCountries.success currency:{}",currency);
        return new ArrayList<>();
    }
}
