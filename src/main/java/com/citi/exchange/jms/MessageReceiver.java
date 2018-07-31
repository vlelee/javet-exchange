package com.citi.exchange.jms;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.io.File;

@Component
public class MessageReceiver {

    @Autowired
    ConfigurableApplicationContext context;

    @JmsListener(destination = "OrderBroker_Reply", containerFactory = "myJmsContainerFactory")
    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
        context.close();
        //TODO: save the transaction response?
        FileSystemUtils.deleteRecursively(new File("activemq-data"));
    }
}