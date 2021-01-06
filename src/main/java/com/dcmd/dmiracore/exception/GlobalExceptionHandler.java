package com.dcmd.dmiracore.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<JsonResponse> handleRuntimeException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(new JsonResponse(ex.getMessage(), 400), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private static class JsonResponse {
        String message;
        int status;

        public JsonResponse(String message, int status) {
            super();
            this.message = message;
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int httpStatus) {
            this.status = httpStatus;
        }

    }
}
