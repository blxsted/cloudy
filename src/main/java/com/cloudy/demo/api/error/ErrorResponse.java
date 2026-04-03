package com.cloudy.demo.api.error;

import java.util.List;

public record ErrorResponse(
        String message,
        List<FieldError> fieldErrors
) {
    public record FieldError(String field, String message) {}
}