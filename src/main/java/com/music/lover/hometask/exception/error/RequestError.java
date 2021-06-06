package com.music.lover.hometask.exception.error;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class RequestError {

    @JacksonXmlProperty
    private String httpStatus;

    @JacksonXmlProperty
    private String errorName;

    @JacksonXmlProperty
    private String text;

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
