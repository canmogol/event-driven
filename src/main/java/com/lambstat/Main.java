package com.lambstat;

import com.lambstat.core.service.EventDispatcher;

public class Main {

    public static void main(String[] args) {
        EventDispatcher eventDispatcher = new EventDispatcher();
        new Thread(eventDispatcher).start();
    }

}
