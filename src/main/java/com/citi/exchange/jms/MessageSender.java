package com.citi.exchange.jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;


@Service
public class MessageSender {

    @Autowired
    private JmsTemplate jmsTemplate;


    public void send(final String orderMessage) {
        System.out.println("sending message: " + orderMessage);
        this.jmsTemplate.convertAndSend("OrderBroker", orderMessage);
        MessageCreator message = new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                Message message = session.createTextMessage(orderMessage);
                return message;
            }
        };
        System.out.println(message);
//        this.jmsTemplate.send("OrderBroker", message);
    }

}
