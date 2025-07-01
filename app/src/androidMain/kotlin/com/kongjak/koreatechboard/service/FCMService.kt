package com.kongjak.koreatechboard.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.benasher44.uuid.Uuid
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kongjak.koreatechboard.MainActivity
import com.kongjak.koreatechboard.R
import com.kongjak.koreatechboard.domain.usecase.database.InsertMultipleNewNoticesUseCase
import com.kongjak.koreatechboard.util.isDebug
import com.kongjak.koreatechboard.util.routes.BoardItem
import com.kongjak.koreatechboard.util.routes.Department
import koreatech_board.app.generated.resources.Res.string
import koreatech_board.app.generated.resources.department_notification_channel_id
import koreatech_board.app.generated.resources.department_notification_channel_name
import koreatech_board.app.generated.resources.dorm_notification_channel_id
import koreatech_board.app.generated.resources.dorm_notification_channel_name
import koreatech_board.app.generated.resources.new_notice_notification_content
import koreatech_board.app.generated.resources.new_notice_notification_title
import koreatech_board.app.generated.resources.school_notification_channel_id
import koreatech_board.app.generated.resources.school_notification_channel_name
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.StringResource
import org.koin.android.ext.android.inject

class FCMService : FirebaseMessagingService() {
    val insertMultipleNewNoticesUseCase: InsertMultipleNewNoticesUseCase by inject()
    override fun onMessageReceived(message: RemoteMessage) {
        if (message.data["new_articles"] != null && message.data["new_articles"]!!.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                runCatching {
                    insertMultipleNewNoticesUseCase(
                        message.data["new_articles"]?.split(":")?.takeIf { it.isNotEmpty() }
                            ?.map { Uuid.fromString(it) } ?: emptyList(),
                        message.data["department"] ?: "school",
                        message.data["board"] ?: "notice"
                    )
                }.onSuccess {
                    sendNotification(message)
                }
            }
        }
    }

    private fun sendNotification(message: RemoteMessage) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("screen", "notice")
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val department = Department.valueOf(message.data["department"] ?: "school")
        val board = BoardItem.valueOf(message.data["board"] ?: "notice")

        val channelId = when (department) {
            Department.School -> getString(string.school_notification_channel_id)
            Department.Dorm -> getString(string.dorm_notification_channel_id)
            else -> getString(string.department_notification_channel_id)
        }

        val channelName = when (department) {
            Department.School -> getString(string.school_notification_channel_name)
            Department.Dorm -> getString(string.dorm_notification_channel_name)
            else -> getString(string.department_notification_channel_name)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(getString(string.new_notice_notification_title))
            .setContentText(
                getString(
                    string.new_notice_notification_content,
                    getString(department.stringResource),
                    getString(board.stringResource)
                )
            )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        manager.notify(Integer.parseInt(channelId), notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        if (isDebug()) {
            Log.d("FCM", "Refreshed token: $token")
        }
    }
}

fun getString(resourceId: StringResource, vararg formatArgs: Any): String = runBlocking {
    org.jetbrains.compose.resources.getString(resourceId, *formatArgs)
}
