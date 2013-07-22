package com.octo.android.robospice.notification;

import android.app.Notification;
import android.content.Context;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestProgress;

public interface NotificationFactory<RESULT> {

    Notification createNotificationForSuccess(Context context, RESULT result);

    Notification createNotificationForFailure(Context context, SpiceException e);

    Notification createNotificationForCancellation(Context context);

    Notification createNotificationForProgressUpdate(Context context,
            RequestProgress progress);
}
