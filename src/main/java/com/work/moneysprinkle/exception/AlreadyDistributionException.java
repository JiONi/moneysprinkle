package com.work.moneysprinkle.exception;

public class AlreadyDistributionException extends RuntimeException{
    public AlreadyDistributionException(String msg){
        super(msg);
    }

    public AlreadyDistributionException(){

    }
}
