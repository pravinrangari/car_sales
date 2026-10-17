package com.codehunt.carsales.service;

import com.codehunt.carsales.dto.MonthlyCountDto;
import com.codehunt.carsales.dto.UploadSalesResponse;
import com.codehunt.carsales.dto.YearlyCountDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CarSalesService {

    UploadSalesResponse uploadCsv(MultipartFile file);

    List<YearlyCountDto> getYearlyCarsCount();

    List<MonthlyCountDto> getMonthlyCountByYear(int year);
}