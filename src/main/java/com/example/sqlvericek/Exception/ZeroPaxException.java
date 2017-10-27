package com.example.sqlvericek.Exception;

public class ZeroPaxException extends Exception {
    public ZeroPaxException(String message){
        super(message); //üst sınıfa mesaj koyar yani exceptiona
    }
}
