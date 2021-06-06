package com.music.lover.hometask.controller;

import com.music.lover.hometask.exception.RequestException;
import com.music.lover.hometask.exception.error.ApplicationError;
import com.music.lover.hometask.exception.error.RequestError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = {RequestException.class})
    public ResponseEntity<RequestError> publicExceptionHandler(RequestException e) {
        if (e.getHttpStatus().is4xxClientError()) {
            logger.warn("httpStatus={}, errorName={}, errorMessage={}, detailMessage={}",
                    e.getHttpStatus(), e.getErrorName(), e.getErrorMessage(), e.getMessage());
        } else {
            logger.error("httpStatus={}, errorName={}, errorMessage={}, detailMessage={}",
                    e.getHttpStatus(), e.getErrorName(), e.getErrorMessage(), e.getMessage());
            logger.error(e.getMessage(), e);
        }
        RequestError requestError = new RequestError();
        requestError.setHttpStatus(e.getHttpStatus().toString());
        requestError.setErrorName(e.getErrorName());
        requestError.setText(e.getErrorMessage());

        return new ResponseEntity<>(requestError, new HttpHeaders(),
                e.getHttpStatus() != null ? e.getHttpStatus() : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<RequestError> anyExceptionHandler(Exception e) {
        return publicExceptionHandler(new RequestException(ApplicationError.SYS_ERR, e));
    }

}
