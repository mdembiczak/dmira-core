package com.dcmd.dmiracore.payload.messages;

public class MessageResponse extends GenericMessage {

    public MessageResponse(String message, Integer status) {
        super(message, status);
    }
}
