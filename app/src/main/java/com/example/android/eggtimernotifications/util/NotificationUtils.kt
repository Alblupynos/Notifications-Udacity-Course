/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.eggtimernotifications.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.example.android.eggtimernotifications.MainActivity
import com.example.android.eggtimernotifications.R
import com.example.android.eggtimernotifications.receiver.SnoozeReceiver

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(messageBody: String, appContext: Context) {

    // Create the content intent for the notification, which launches
    // this activity
    // Step 1.11 create intent
    val contentIntent = Intent(appContext, MainActivity::class.java)

    // Step 1.12 create PendingIntent
    val contentPendingIntent = PendingIntent.getActivity(
        appContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    // Step 2.0 add style
    val eggImage = BitmapFactory.decodeResource(
        appContext.resources,
        R.drawable.cooked_egg
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(eggImage)
        .bigLargeIcon(null)

    // Step 2.2 add snooze action
    val snoozeIntent = Intent(appContext, SnoozeReceiver::class.java)
    val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
        appContext,
        REQUEST_CODE,
        snoozeIntent,
        FLAGS
    )

    // Step 1.2 get an instance of NotificationCompat.Builder
    // Build the notification
    // Step 1.8 use the new 'breakfast' notification channel
    val builder = NotificationCompat.Builder(
        appContext,
        appContext.getString(R.string.egg_notification_channel_id)
    )

        // Step 1.3 set title, text and icon to builder
        .setSmallIcon(R.drawable.cooked_egg)
        .setContentTitle(appContext.getString(R.string.notification_title))
        .setContentText(messageBody)

        // Step 1.13 set content intent
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

        // Step 2.1 add style to builder
        .setStyle(bigPicStyle)
        .setLargeIcon(eggImage)

        // Step 2.3 add snooze action
        .addAction(
            R.drawable.egg_icon,
            appContext.getString(R.string.snooze),
            snoozePendingIntent
        )

        // Step 2.5 set priority. To support devices running API level 25 or lower.
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    // Step 1.4 call notify
    notify(NOTIFICATION_ID, builder.build())
}

