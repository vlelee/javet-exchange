package com.citi.exchange;
//
import com.citi.exchange.algorithms.TradeExecution;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.File;

@EntityScan("com.citi.exchange.entities")
@Configuration
@EnableWebMvc
@EnableScheduling
@SpringBootApplication
//@EnableJms
public class ExchangeApplication extends WebMvcConfigurerAdapter {


    public static void main(String[] args) {
        SpringApplication.run(com.citi.exchange.ExchangeApplication.class, args);

    }

	@Bean
		// Strictly speaking this bean is not necessary as boot creates a default
	JmsListenerContainerFactory<?> myJmsContainerFactory(ConnectionFactory connectionFactory) {
		SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		return factory;
	}
//		// Clean out any ActiveMQ data from a previous run
//		FileSystemUtils.deleteRecursively(new File("activemq-data"));
//
//		// Launch the application
//		ConfigurableApplicationContext context = SpringApplication.run(ExchangeApplication.class, args);
//		String msg = (new TradeExecution()).createMessage(true, 123, 11.50, 200, "GOOGL",  new java.sql.Timestamp(System.currentTimeMillis()) );
//
//		// Send a message
//		MessageCreator messageCreator = new MessageCreator() {
//			@Override
//			public Message createMessage(Session session) throws JMSException {
//				return session.createTextMessage(msg);
//			}
//		};
//		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
//		System.out.println("Sending a new message.");
//		jmsTemplate.send("OrderBroker", messageCreator);



    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer matcher) {
        matcher.setUseRegisteredSuffixPatternMatch(true);
    }

}
