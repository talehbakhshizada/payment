package com.ms.country.controller;

import com.ms.country.model.CountryResponseDto;
import com.ms.country.service.CountryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/countries")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CountryController {

    CountryService countryService;

    @GetMapping
    public List<CountryResponseDto> getAvailableCountries(@RequestParam String currency) {
       return countryService.getAvailableCountries(currency);
    }

}
