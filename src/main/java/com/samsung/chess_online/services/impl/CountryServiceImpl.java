package com.samsung.chess_online.services.impl;

import com.samsung.chess_online.domain.Country;
import com.samsung.chess_online.exception.CountryNotFoundException;
import com.samsung.chess_online.repository.CountryRepository;
import com.samsung.chess_online.services.CountryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CountryServiceImpl implements CountryService {
    private final CountryRepository repository;

    public CountryServiceImpl(CountryRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Country getById(long id) {
        return repository.findById(id).orElseThrow(() -> new CountryNotFoundException("country with id'" + id + "' was not found"));
    }
}
