package com.lambstat;

import com.lambstat.core.service.EventDispatcher;

import java.io.IOException;
import java.util.logging.LogManager;

public class Main {

    public static void main(String[] args) {
        try {
            LogManager.getLogManager().readConfiguration(Main.class.getClassLoader().getResourceAsStream("logging.properties"));
        } catch (IOException e) {
            // we can safely ignore this exception since it is purely cosmetics
        }
        EventDispatcher eventDispatcher = new EventDispatcher();
        new Thread(eventDispatcher).start();
    }

}
