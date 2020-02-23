package com.djedra.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.djedra.entity.Rate;

public interface IRateRepository extends JpaRepository<Rate, Long> {

	Rate findFirstByCurrency_CodeAndDateBetweenOrderByMidDesc(String code, LocalDate dateFrom, LocalDate dateTo);

	List<Rate> findTop5ByCurrency_CodeAndDateBetweenOrderByMidDesc(String code, LocalDate dateFrom, LocalDate dateTo);

	List<Rate> findTop5ByCurrency_CodeAndDateBetweenOrderByMidAsc(String code, LocalDate dateFrom, LocalDate dateTo);
}
