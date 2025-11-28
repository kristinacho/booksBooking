package com.books.notifications;

import java.util.HashMap;
import java.util.Map;

public class CachingNotificationDecorator extends NotificationDecorator {
    private static final Map<String, String> cache = new HashMap<>();

    public CachingNotificationDecorator(Notification notification) {
        super(notification);
    }

    @Override
    public void send() {
        String cacheKey = getType() + ":" + getMessage().hashCode();

        if (cache.containsKey(cacheKey)) {
            System.out.println("💾 Использовано кэшированное уведомление: " + cache.get(cacheKey));
            return;
        }

        super.send();
        cache.put(cacheKey, getMessage());
        System.out.println("💾 Уведомление сохранено в кэш");
    }
}