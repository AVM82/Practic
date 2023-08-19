package com.group.practic.service;


import com.group.practic.dto.SendMessageDto;

public interface Sender {
    void sendMessage(SendMessageDto sendMessageDto);
}
