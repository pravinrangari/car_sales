package com.codehunt.carsales.service;

import com.codehunt.carsales.dto.MonthlyCountDto;
import com.codehunt.carsales.dto.UploadSalesResponse;
import com.codehunt.carsales.dto.YearlyCountDto;
import com.codehunt.carsales.entity.CarSales;
import com.codehunt.carsales.repository.CarSalesRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CarSalesServiceImpl implements CarSalesService {

    private final CarSalesRepository repository;

    public CarSalesServiceImpl(CarSalesRepository repository) {
        this.repository = repository;
    }


    @Override
    public UploadSalesResponse uploadCsv(MultipartFile file) {

        List<CarSales> cars = new ArrayList<>();

        int failCount = 0;
        int totalRecords = 0;


        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                file.getInputStream(),
                                StandardCharsets.UTF_8
                        )
                )
        ) {


            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setIgnoreHeaderCase(true)
                    .setTrim(true)
                    .build();


            CSVParser parser = CSVParser.parse(reader, csvFormat);


            for (CSVRecord record : parser) {

                totalRecords++;

                try {

                    System.out.println(record.toMap());


                    if(repository.existsByCarNumber(
                            record.get("Car Number")
                    )) {
                        failCount++;
                        continue;
                    }


                    CarSales car = new CarSales();


                    car.setCarNumber(
                            record.get("Car Number")
                    );

                    car.setBrand(
                            record.get("Brand")
                    );

                    car.setModel(
                            record.get("Model")
                    );

                    car.setColor(
                            record.get("Color")
                    );


                    car.setYear(
                            Integer.parseInt(
                                    record.get("Year")
                            )
                    );


                    car.setDateOfPurchase(
                            LocalDate.parse(
                                    record.get("Date of Purchase"),
                                    DateTimeFormatter.ofPattern("dd-MM-yyyy")
                            )
                    );


                    car.setTimeOfPurchase(
                            LocalTime.parse(
                                    record.get("Time of Purchase")
                            )
                    );


                    car.setPrice(
                            Long.parseLong(
                                    record.get("Price (Rs)")
                            )
                    );


                    car.setMileage(
                            Double.parseDouble(
                                    record.get("Mileage (km/l)")
                            )
                    );


                    // IMPORTANT FIX
                    car.setEngine(
                            Integer.parseInt(
                                    record.get("Engine (cc)")
                            )
                    );


                    car.setFuelType(
                            record.get("Fuel Type")
                    );


                    car.setPaymentMode(
                            record.get("Payment Mode")
                    );


                    car.setState(
                            record.get("State")
                    );


                    car.setCity(
                            record.get("City")
                    );


                    car.setCustomerName(
                            record.get("Customer Name")
                    );


                    car.setContactNumber(
                            record.get("Contact Number")
                    );


                    car.setEmail(
                            record.get("Email")
                    );


                    car.setWarrantyPeriod(
                            Integer.parseInt(
                                    record.get("Warranty Period (years)")
                            )
                    );


                    cars.add(car);


                } catch(Exception e) {

                    e.printStackTrace();
                    failCount++;

                }

            }


            if(!cars.isEmpty()) {
                repository.saveAll(cars);
            }


        } catch(Exception e) {

            throw new RuntimeException(
                    "CSV upload failed : " + e.getMessage(),
                    e
            );
        }


        return new UploadSalesResponse(
                totalRecords,
                totalRecords - failCount,
                failCount
        );
    }



    @Override
    public List<YearlyCountDto> getYearlyCarsCount() {

        return repository.getYearlyCount();

    }



    @Override
    public List<MonthlyCountDto> getMonthlyCountByYear(int year) {


        List<MonthlyCountDto> data =
                repository.getMonthlyCountByYear(year);


        Map<Integer, Long> map =
                data.stream()
                        .collect(Collectors.toMap(
                                MonthlyCountDto::month,
                                MonthlyCountDto::count
                        ));


        List<MonthlyCountDto> result =
                new ArrayList<>();


        for(int month = 1; month <= 12; month++) {

            result.add(
                    new MonthlyCountDto(
                            month,
                            map.getOrDefault(month, 0L)
                    )
            );

        }


        return result;

    }
}