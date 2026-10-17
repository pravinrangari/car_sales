package com.codehunt.carsales.commons.response;
import com.codehunt.carsales.dto.UploadSalesResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<com.codehunt.carsales.commons.response.ApiResponse<UploadSalesResponse>> handleAllExceptions(Exception ex) {

        UploadSalesResponse response = new UploadSalesResponse(0, 0, 0);

        com.codehunt.carsales.commons.response.ApiResponse<UploadSalesResponse> apiResponse =
                new com.codehunt.carsales.commons.response.ApiResponse<>(
                        false,
                        ex.getMessage(),
                        response,
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                );

        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}