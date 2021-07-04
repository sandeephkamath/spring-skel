package com.cepheid.cloud.skel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.cepheid.cloud.skel.exception.ItemNotFoundException.REASON;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = REASON)
public class DescriptionNotFoundException extends RuntimeException {
    public static final String REASON = "Description not found";
}
