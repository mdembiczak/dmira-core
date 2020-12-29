package com.dcmd.dmiracore.payload.response;

public abstract class GenericMessage {
    private String message;
    private Integer status;

    protected GenericMessage(String message, Integer status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }
}
