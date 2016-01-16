package com.lambstat.module.buildin.authentication.service;

import com.lambstat.core.event.Event;
import com.lambstat.core.service.AbstractService;
import com.lambstat.module.buildin.authentication.event.AuthenticationRequestEvent;
import com.lambstat.module.buildin.authentication.event.AuthenticationResponseEvent;

import java.util.HashSet;

public class AuthenticationService extends AbstractService {

    public AuthenticationService() {
        super(new HashSet<Class<? extends Event>>() {{
            add(AuthenticationRequestEvent.class);
            add(AuthenticationResponseEvent.class);
        }});
    }

}
