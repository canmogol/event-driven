package com.lambstat.core.log;

import java.util.logging.Logger;

public abstract class BaseLogger {

    protected abstract Logger getLogger();

    protected void log(String log) {
        getLogger().info("[" + Thread.currentThread().getId() + "] [" + getClass().getSimpleName() + "] " + log);
    }

    protected void error(String log) {
        getLogger().severe("[" + Thread.currentThread().getId() + "] [" + getClass().getSimpleName() + "] " + log);
    }

}
