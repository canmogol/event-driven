package com.lambstat.module.buildin.datastore.event;

import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.event.Event;

public class QueryDataResultEvent extends BaseEvent {

    private String key;
    private Object value;

    public QueryDataResultEvent(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public QueryDataResultEvent(Event parent, String key, Object value) {
        super(parent);
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
