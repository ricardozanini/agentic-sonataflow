package org.acme.agentic.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LogNotificationService implements NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogNotificationService.class);

    @Override
    public boolean notifyPooling(String address, String message) {
        LOGGER.info("Recipient {} \n ------ \n {} \n ------", address, message);
        return true;
    }
}
