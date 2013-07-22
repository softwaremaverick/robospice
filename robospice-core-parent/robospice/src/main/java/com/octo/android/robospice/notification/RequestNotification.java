package com.octo.android.robospice.notification;

public interface RequestNotification<RESULT> {

    int getNotificationId();

    NotificationFactory<RESULT> getNotificationFactory();
}
