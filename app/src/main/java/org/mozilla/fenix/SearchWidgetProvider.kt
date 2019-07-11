package org.mozilla.fenix

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews

class SearchWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetIds.forEach { appWidgetId ->
            val pendingIntent: PendingIntent = Intent(context, IntentReceiverActivity::class.java)
                .let { intent ->
                    intent.action = Intent.ACTION_VIEW
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra(HomeActivity.OPEN_TO_SEARCH, true)
                    PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
                }

            val pendingIntent2: PendingIntent = Intent(context, IntentReceiverActivity::class.java)
                .let { intent ->
                    intent.action = Intent.ACTION_VIEW
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra(IntentReceiverActivity.SPEECH_PROCESSING, true)
                    PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT)
                }

            val views: RemoteViews = RemoteViews(context.packageName, R.layout.search_widget)
                .apply {
                    setOnClickPendingIntent(R.id.button, pendingIntent)
                    setOnClickPendingIntent(R.id.button2, pendingIntent2)
                }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}