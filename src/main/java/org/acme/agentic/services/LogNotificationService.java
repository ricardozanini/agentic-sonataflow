package org.acme.agentic.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LogNotificationService implements NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogNotificationService.class);

    @Tool("send notifications to users based on their address")
    @Override
    public boolean notifyPooling(String address, String title, String message) {
        LOGGER.info("Recipient {} (Title: {}) \n ------ \n {} \n ------", address, title, message);
        return true;
    }
}
