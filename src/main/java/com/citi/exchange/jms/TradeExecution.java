package com.citi.exchange.jms;

import com.citi.exchange.entities.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.File;
import java.text.SimpleDateFormat;

@Service
public class TradeExecution {

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * Creates a JMS formatted message to send to the OrderBroker and places the message in the queue
     * @param trade a Trade object that contains the stock and price to trade
     */
    public void send(Trade trade) {
       String msg = "<trade>\n" +
                "<buy>" + trade.isBuying() + "</buy>\n" +
                "<id>" + trade.getId() + "</id>\n" +
                "<price>" + trade.getTradePrice() + "</price>\n" +
                "<size>" + trade.getNumShares() + "</size>\n" +
                "<stock>" + trade.getStock().getTicker().trim() + "</stock>\n" +
                "<whenAsDate>" + (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")).format(System.currentTimeMillis()) + "</whenAsDate>\n" +
                "</trade>";

       MessageCreator orderMessage = new MessageCreator() {
            @Override
            public javax.jms.Message createMessage(Session session) throws JMSException {
                javax.jms.Message message = session.createTextMessage(msg);
                message.setJMSCorrelationID("JAVET"+trade.getId());
                return message;
            }
        };
        this.jmsTemplate.send("OrderBroker", orderMessage);
    }


    @Autowired
    ConfigurableApplicationContext context;
    @Autowired
    private com.citi.exchange.services.TradeService tradeService;

    /**
     * Receives and parses a message from OrderBroerk and saves the message to the trade
     * @param message the JMS message that the Order Broker returns from the responding queu
     * @throws JMSException
     */
    @JmsListener(destination = "OrderBroker_Reply", containerFactory = "myJmsContainerFactory")
    public void receiveMessage(javax.jms.Message message) throws JMSException {
        String responseMessage = ((TextMessage) message).getText();
        int tradeId = Integer.parseInt(message.getJMSCorrelationID().split("JAVET")[1]);
        tradeService.updateTradeResponse(responseMessage, tradeId);
        FileSystemUtils.deleteRecursively(new File("activemq-data"));
    }

}
