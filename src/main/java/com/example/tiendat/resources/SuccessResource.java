package com.example.tiendat.resources;

public class SuccessResource <T>{
    
    private String message;

    private T data;

    public SuccessResource(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setData(T data) {
        this.data = data;
    }
}
