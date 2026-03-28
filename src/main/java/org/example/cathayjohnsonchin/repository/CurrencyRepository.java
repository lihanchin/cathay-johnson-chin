package org.example.cathayjohnsonchin.repository;

import org.example.cathayjohnsonchin.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

    Optional<Currency> findCurrencyByCode(String code);

    boolean existsByCode(String code);
}
