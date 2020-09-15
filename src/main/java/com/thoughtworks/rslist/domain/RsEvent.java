package com.thoughtworks.rslist.domain;

public class RsEvent {
    String eventName;
    String keyWord;

    public RsEvent(String eventName, String keyWord) {
        this.eventName = eventName;
        this.keyWord = keyWord;
    }
    public RsEvent(){}

    public String getEventName() {
        return eventName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
