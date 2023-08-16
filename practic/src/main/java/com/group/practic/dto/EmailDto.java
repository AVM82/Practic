package com.group.practic.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmailDto {

    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}