package com.work.moneysprinkle.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(SprinkleSameUserException.class)
    public ResponseEntity<ApiErrorResponse> sprinkleSameUserException(SprinkleSameUserException ex){
        ApiErrorResponse response = new ApiErrorResponse("001", "Cannot receive who's that sprinkled money");
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(NotAvailableSprinkleException.class)
    public ResponseEntity<ApiErrorResponse> notAvailableSprinkleException(NotAvailableSprinkleException ex){
        ApiErrorResponse response = new ApiErrorResponse("002", "Not available sprinkled.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotAvailableViewException.class)
    public ResponseEntity<ApiErrorResponse> notAvailableViewException(NotAvailableViewException ex){
        ApiErrorResponse response = new ApiErrorResponse("003", "Cannot view info that sprinkled.");
        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(AlreadyDistributionException.class)
    public ResponseEntity<ApiErrorResponse> alreadyDistributionException(AlreadyDistributionException ex){
        ApiErrorResponse response = new ApiErrorResponse("004", "Already received which sprinkled.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotExistRemainMoneyException.class)
    public ResponseEntity<ApiErrorResponse> notExistRemainMoneyException(NotExistRemainMoneyException ex){
        ApiErrorResponse response = new ApiErrorResponse("005", "Not exist money that can received.");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
