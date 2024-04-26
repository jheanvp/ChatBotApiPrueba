package com.example.chatbotapiprueba.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
@Setter
public class ApiException extends ResponseStatusException {

    private String messageCustom;

    public ApiException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public ApiException(HttpStatus status, String message, String reason) {
        super(status, reason);
        this.messageCustom = message;
    }

}
