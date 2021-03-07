package com.widgets.rest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WidgetServiceApp {

    private static Logger logger = LoggerFactory.getLogger(WidgetServiceApp.class.getSimpleName());

    public static void main(String[] args) {
        SpringApplication.run(WidgetServiceApp.class, args);
        logger.info("Application Started");
    }

}
