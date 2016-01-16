package com.lambstat.module.buildin.datastore.service;

import com.lambstat.core.event.Event;
import com.lambstat.core.service.AbstractService;
import com.lambstat.module.buildin.datastore.event.QueryDataRequestEvent;
import com.lambstat.module.buildin.datastore.event.QueryDataResultEvent;
import com.lambstat.module.buildin.datastore.event.RemoveDataEvent;
import com.lambstat.module.buildin.datastore.event.StoreDataEvent;

import java.util.HashSet;

public class DataStoreService extends AbstractService {

    public DataStoreService() {
        super(new HashSet<Class<? extends Event>>() {{
            add(QueryDataRequestEvent.class);
            add(QueryDataResultEvent.class);
            add(RemoveDataEvent.class);
            add(StoreDataEvent.class);
        }});
    }


}
