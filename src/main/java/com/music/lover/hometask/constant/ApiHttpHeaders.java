package com.music.lover.hometask.constant;

public enum ApiHttpHeaders {
    USER_KEY_HEADER("user");

    private final String headerValue;

    ApiHttpHeaders(String headerValue) {
        this.headerValue = headerValue;
    }

    public String getHeaderValue() {
        return headerValue;
    }

}
