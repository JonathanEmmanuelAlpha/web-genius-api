package com.webgenius.webgeniusapi.utils;

import lombok.Value;

@Value
public class Message {

    private String responseMessage;

    public static Message from(final String responseMessage) {
        return new Message(responseMessage);
    }
}