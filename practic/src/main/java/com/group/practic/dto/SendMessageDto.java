package com.group.practic.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


public class SendMessageDto {

    @NotBlank
    @Min(5)
    @Email
    private String address;

    @NotBlank
    @Min(5)
    private String message;

    @NotBlank
    @Min(5)
    private String header;


    public SendMessageDto() {
    }


    public SendMessageDto(@NotBlank @Min(5) @Email String address, @NotBlank @Min(5) String message,
            @NotBlank @Min(5) String header) {
        super();
        this.address = address;
        this.message = message;
        this.header = header;
    }


    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    public String getHeader() {
        return header;
    }


    public void setHeader(String header) {
        this.header = header;
    }

}
