package com.citi.exchange;

import com.citi.exchange.algorithms.StrategyExecution;
import com.citi.exchange.entities.StockPrice;
import com.citi.exchange.entities.StrategyConfiguration;
import com.citi.exchange.services.StockPriceWebService;
import com.citi.exchange.services.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.jms.ConnectionFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@EntityScan("com.citi.exchange.entities")
//@ComponentScan("com.citi.exchange.jms")
@EnableScheduling
@SpringBootApplication
@Configuration
@EnableJms
public class ExchangeApplication extends WebMvcConfigurerAdapter {
    public static void main(String[] args) {
        SpringApplication.run(com.citi.exchange.ExchangeApplication.class, args);
//        ConfigurableApplicationContext context = SpringApplication.run(ExchangeApplication.class, args);
    }


    @Bean
        // Strictly speaking this bean is not necessary as boot creates a default
    JmsListenerContainerFactory<?> myJmsContainerFactory(ConnectionFactory connectionFactory) {
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer matcher) {
        matcher.setUseRegisteredSuffixPatternMatch(true);
    }

}
