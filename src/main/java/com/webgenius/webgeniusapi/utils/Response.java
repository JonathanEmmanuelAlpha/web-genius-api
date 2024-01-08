package com.webgenius.webgeniusapi.utils;

import lombok.Value;

@Value
public class Response {

    private String message;

    private ResponseType type;

    public static Response from(final String message, final ResponseType type) {

        return new Response(message, type);
    }
}
