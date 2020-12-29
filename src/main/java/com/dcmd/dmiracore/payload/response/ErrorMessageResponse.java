package com.dcmd.dmiracore.payload.response;

public class ErrorMessageResponse extends GenericMessage {
    public ErrorMessageResponse(String message, Integer status) {
        super(message, status);
    }
}
