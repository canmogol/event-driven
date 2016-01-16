package com.lambstat.module.buildin.authorization.service;

import com.lambstat.core.event.Event;
import com.lambstat.core.service.AbstractService;
import com.lambstat.module.buildin.authorization.event.AuthorizationRequestEvent;
import com.lambstat.module.buildin.authorization.event.AuthorizationResultEvent;

import java.util.HashSet;

public class AuthorizationService extends AbstractService {

    public AuthorizationService() {
        super(new HashSet<Class<? extends Event>>() {{
            add(AuthorizationRequestEvent.class);
            add(AuthorizationResultEvent.class);
        }});
    }


}
