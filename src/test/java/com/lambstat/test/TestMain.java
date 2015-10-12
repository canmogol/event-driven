package com.lambstat.test;

import com.lambstat.core.service.EventDispatcher;

public class TestMain {

    public static void main(String[] args) {
        EventDispatcher eventDispatcher = new EventDispatcher();
        new Thread(eventDispatcher).start();

        // EventProducerTester mimics execution, just for testing purposes
        new Thread(new EventProducerTester(eventDispatcher)).start();
    }

}
