package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RsEvent {

    @NotNull
    private String eventName;
    @NotNull
    private String keyWord;
    @Valid
    private int userID;


    public String getEventName() {
        return eventName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    //@JsonIgnore
    public int getUser() {
        return userID;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    //@JsonProperty
    public void setUser(User user) {
        this.userID = userID;
    }
}
