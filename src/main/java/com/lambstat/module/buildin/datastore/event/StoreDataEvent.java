package com.lambstat.module.buildin.datastore.event;

import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.event.Event;

public class StoreDataEvent extends BaseEvent {

    private String key;
    private Object data;

    public StoreDataEvent(String key, Object data) {
        this.key = key;
        this.data = data;
    }

    public StoreDataEvent(Event parent, String key, Object data) {
        super(parent);
        this.key = key;
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public Object getData() {
        return data;
    }

}
