package com.samsung.chess_online.repository;

import com.samsung.chess_online.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> getById(long id);
}
