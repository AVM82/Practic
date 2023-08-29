package com.group.practic.dto;

public class MessageSendingResultDto {

    private String message;

    private int successfulDeliveries;


    public MessageSendingResultDto() {
    }


    public MessageSendingResultDto(String message, int successfulDeliveries) {
        super();
        this.message = message;
        this.successfulDeliveries = successfulDeliveries;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    public int getSuccessfulDeliveries() {
        return successfulDeliveries;
    }


    public void setSuccessfulDeliveries(int successfulDeliveries) {
        this.successfulDeliveries = successfulDeliveries;
    }

}
