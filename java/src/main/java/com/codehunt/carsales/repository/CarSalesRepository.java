package com.codehunt.carsales.repository;

import com.codehunt.carsales.dto.MonthlyCountDto;
import com.codehunt.carsales.dto.YearlyCountDto;
import com.codehunt.carsales.entity.CarSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarSalesRepository extends JpaRepository<CarSales, Long> {

    boolean existsByCarNumber(String carNumber);

    @Query("SELECT new com.codehunt.carsales.dto.YearlyCountDto(c.year, COUNT(c)) " +
            "FROM CarSales c " +
            "GROUP BY c.year " +
            "ORDER BY c.year")
    List<YearlyCountDto> getYearlyCount();

    @Query("SELECT new com.codehunt.carsales.dto.MonthlyCountDto(" +
            "MONTH(c.dateOfPurchase), COUNT(c)) " +
            "FROM CarSales c " +
            "WHERE YEAR(c.dateOfPurchase) = :year " +
            "GROUP BY MONTH(c.dateOfPurchase) " +
            "ORDER BY MONTH(c.dateOfPurchase)")
    List<MonthlyCountDto> getMonthlyCountByYear(@Param("year") int year);

}