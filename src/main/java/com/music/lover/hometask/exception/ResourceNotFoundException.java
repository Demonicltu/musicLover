package com.music.lover.hometask.exception;

import com.music.lover.hometask.exception.error.ApplicationError;

public class ResourceNotFoundException extends RequestException {

    public ResourceNotFoundException() {
        super(ApplicationError.RESOURCE_NOT_FOUND);
    }

}
