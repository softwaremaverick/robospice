package com.octo.android.robospice.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.listener.RequestProgress;
import com.octo.android.robospice.request.observer.RequestObserver;
import com.octo.android.robospice.request.observer.RequestObserverFactory;

public class RequestNotificationObserver implements RequestObserver {

    public static class Builder implements RequestObserverFactory {
        private final NotificationManager nManager;
        private final Context context;

        public Builder(Context context) {
            this.context = context;

            nManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
        }

        @Override
        public RequestObserver create(CachedSpiceRequest<?> request) {
            if (request.getSpiceRequest() instanceof RequestNotification) {
                return new RequestNotificationObserver(context, nManager,
                        (RequestNotification<?>) request.getSpiceRequest());
            }

            return null;
        }
    }

    private final NotificationManager nManager;
    private final Context context;
    private final RequestNotification<?> requestNotification;
    private final NotificationFactory<?> notificationFactory;
    private final int notificationId;

    public <RESULT> RequestNotificationObserver(Context context,
            NotificationManager nManager,
            RequestNotification<RESULT> requestNotification) {

        this.context = context;
        this.nManager = nManager;

        this.requestNotification = requestNotification;
        this.notificationFactory = requestNotification.getNotificationFactory();
        this.notificationId = requestNotification.getNotificationId();
    }

    @Override
    public void onRequestAdded(CachedSpiceRequest<?> request) {
    }

    @Override
    public <RESULT> void onRequestCompleted(CachedSpiceRequest<RESULT> request,
            RESULT result) {

        RequestNotification<RESULT> requestNotificationCastToResult = (RequestNotification<RESULT>) this.requestNotification;

        Notification notification = requestNotificationCastToResult
                .getNotificationFactory().createNotificationForSuccess(context, result);

        nManager.notify(requestNotification.getNotificationId(), notification);
    }

    @Override
    public void onRequestFailed(CachedSpiceRequest<?> request, SpiceException e) {
        Notification notification = notificationFactory
                .createNotificationForFailure(context, e);

        nManager.notify(notificationId, notification);
    }

    @Override
    public void onRequestCancelled(CachedSpiceRequest<?> request) {
        Notification notification = notificationFactory
                .createNotificationForCancellation(context);

        nManager.notify(notificationId, notification);
    }

    @Override
    public void onRequestProgressUpdated(CachedSpiceRequest<?> request,
            RequestProgress progress) {

        Notification notification = notificationFactory
                .createNotificationForProgressUpdate(context, progress);

        nManager.notify(notificationId, notification);
    }
}
