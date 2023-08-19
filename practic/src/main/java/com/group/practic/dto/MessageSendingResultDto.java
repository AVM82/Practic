package com.group.practic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class MessageSendingResultDto {
    private String message;
    private int successfulDeliveries;
}
