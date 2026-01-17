package com.radiuk.notification_service.util;

public final class RabbitConstants {

    private RabbitConstants() {}

    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";

    public static final String PASSWORD_RESET_QUEUE = "notification.password-reset.queue";

    public static final String NOTIFICATION_ROUTING_KEY = "password.reset";
}
