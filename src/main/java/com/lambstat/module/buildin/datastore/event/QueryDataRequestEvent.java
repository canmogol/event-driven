package com.lambstat.module.buildin.datastore.event;

import com.lambstat.core.event.BaseEvent;
import com.lambstat.core.event.Event;

public class QueryDataRequestEvent extends BaseEvent {

    private String key;

    public QueryDataRequestEvent(String key) {
        this.key = key;
    }

    public QueryDataRequestEvent(Event parent, String key) {
        super(parent);
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
