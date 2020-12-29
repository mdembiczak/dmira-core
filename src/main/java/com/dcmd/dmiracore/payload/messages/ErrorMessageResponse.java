package com.dcmd.dmiracore.payload.messages;

public class ErrorMessageResponse extends GenericMessage {
    public ErrorMessageResponse(String message, Integer status) {
        super(message, status);
    }
}
