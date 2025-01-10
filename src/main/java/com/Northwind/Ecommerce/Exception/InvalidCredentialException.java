package com.Northwind.Ecommerce.Exception;

public class InvalidCredentialException extends RuntimeException{

    public InvalidCredentialException(String message){

        super(message);
    }
}
