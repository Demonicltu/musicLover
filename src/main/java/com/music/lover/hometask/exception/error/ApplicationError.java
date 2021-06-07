package com.music.lover.hometask.exception.error;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public enum ApplicationError {

    SYS_ERR("System error", INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND("User does not exist", NOT_FOUND),
    SERVICE_UNAVAILABLE("Service is not available at the moment", HttpStatus.SERVICE_UNAVAILABLE),
    PASSWORDS_DONT_MATCH("Passwords do not match", BAD_REQUEST),
    MISSING_USER_KEY("Missing user key header", UNAUTHORIZED),
    USER_ALREADY_EXISTS("Client with this name already exists", BAD_REQUEST),
    RESOURCE_NOT_FOUND("Resource not found", NOT_FOUND);

    private final String errorName;
    private final String message;
    private final HttpStatus httpStatus;

    ApplicationError(String message, HttpStatus httpStatus) {
        this.errorName = this.name();
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getErrorName() {
        return errorName;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
