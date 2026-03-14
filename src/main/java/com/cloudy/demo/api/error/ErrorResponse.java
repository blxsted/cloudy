package com.cloudy.demo.api.error;

import java.util.List;

public class ErrorResponse {

    private String error;
    private List<FieldError> fieldErrors;

    public ErrorResponse(String error, List<FieldError> fieldErrors) {
        this.error = error;
        this.fieldErrors = fieldErrors;
    }

    public String getError() { return error;}

    public List<FieldError> getFieldErrors() { return fieldErrors; }

    public static class FieldError {
        private String field;
        private String message;

        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() { return field; }

        public String getMessage() { return message; }
    }
}
