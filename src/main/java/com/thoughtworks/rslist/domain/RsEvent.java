package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class RsEvent {
    @NotNull
    private String eventName;
    @NotNull
    private String keyWord;
    private User user;


    public RsEvent(String eventName, String keyWord, User user) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.user=user;
    }
    public RsEvent(){}

    public String getEventName() {
        return eventName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    @JsonProperty
    public void setUser(User user) {
        this.user = user;
    }
}
