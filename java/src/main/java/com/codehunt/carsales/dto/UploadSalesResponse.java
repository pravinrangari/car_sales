package com.codehunt.carsales.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UploadSalesResponse {

    private int totalRecords;
    private int successCount;
    private int failedCount;
}