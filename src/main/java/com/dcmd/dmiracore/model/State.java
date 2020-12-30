package com.dcmd.dmiracore.model;

import com.dcmd.dmiracore.model.enums.EState;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "states")
public class State {
    @Id
    private String id;
    private EState state;

    public State() {
    }

    public State(EState state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public EState getState() {
        return state;
    }
}
