package com.work.moneysprinkle.exception;

public class SprinkleSameUserException extends RuntimeException {
    public SprinkleSameUserException(String msg){
        super(msg);
    }
    public SprinkleSameUserException(){

    }
}
