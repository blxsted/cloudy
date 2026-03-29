package com.cloudy.demo.api.error;

import java.util.List;

public class ErrorResponse {

    private String message;
    private List<FieldError> fieldErrors;

    public ErrorResponse(String message, List<FieldError> fieldErrors) {
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public static class FieldError {
        private String field;
        private String message;

        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }
    }

    public String getMessage() {
        return message;
    }
    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }
}
