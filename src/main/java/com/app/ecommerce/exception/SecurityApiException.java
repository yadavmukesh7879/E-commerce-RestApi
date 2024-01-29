package com.app.ecommerce.exception;

import org.springframework.http.HttpStatus;

public class SecurityApiException extends Throwable {
    public SecurityApiException(HttpStatus httpStatus, String invalidJwtSignature) {
    }
}
