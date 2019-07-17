package org.mozilla.fenix

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import org.jetbrains.anko.layoutInflater

class SearchWidgetOneButtonProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {

        val pendingIntent: PendingIntent = Intent(context, HomeActivity::class.java)
            .let { intent ->
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra(HomeActivity.OPEN_TO_SEARCH, true)
                PendingIntent.getActivity(context, 0, intent, 0)
            }

        val views: RemoteViews = RemoteViews(context.packageName, R.layout.search_widget_one_button)
            .apply {
                setOnClickPendingIntent(R.id.button_search_widget_new_tab, pendingIntent)
            }

        appWidgetIds.forEach { appWidgetId ->
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

open class SearchWidgetTwoButtonsProvider(val identifier: SearchWidgetIdentifier) : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val (text, layout) = when (identifier) {
            SearchWidgetIdentifier.SMALL -> Pair("", R.layout.search_widget_small)
            SearchWidgetIdentifier.MEDIUM -> Pair("Search", R.layout.search_widget_medium)
            SearchWidgetIdentifier.LARGE -> Pair("Search the web", R.layout.search_widget_large)
            SearchWidgetIdentifier.EXTRA_LARGE -> Pair("Search the web", R.layout.search_widget_extra_large)
        }

        val pendingIntent: PendingIntent = Intent(context, HomeActivity::class.java)
            .let { intent ->
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra(HomeActivity.OPEN_TO_SEARCH, true)
                PendingIntent.getActivity(context, 0, intent, 0)
            }

        val pendingIntent2: PendingIntent = Intent(context, IntentReceiverActivity::class.java)
            .let { intent ->
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra(IntentReceiverActivity.SPEECH_PROCESSING, true)
                PendingIntent.getActivity(context, 1, intent, 0)
            }

        val views: RemoteViews = RemoteViews(context.packageName, layout)
            .apply {
                setTextViewText(R.id.text_search_widget, text)
                setOnClickPendingIntent(R.id.button_search_widget_new_tab, pendingIntent)
                setOnClickPendingIntent(R.id.button_search_widget_voice, pendingIntent2)
            }

        appWidgetIds.forEach { appWidgetId ->
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    fun adjustLayoutSize() {

    }
}
class SearchWidgetProviderExtraLarge : SearchWidgetTwoButtonsProvider(identifier = SearchWidgetIdentifier.EXTRA_LARGE)

class SearchWidgetProviderSmall : SearchWidgetTwoButtonsProvider(identifier = SearchWidgetIdentifier.SMALL)
class SearchWidgetProviderMedium : SearchWidgetTwoButtonsProvider(identifier = SearchWidgetIdentifier.MEDIUM)
class SearchWidgetProviderLarge : SearchWidgetTwoButtonsProvider(identifier = SearchWidgetIdentifier.LARGE)

enum class SearchWidgetIdentifier {
    SMALL,
    MEDIUM,
    LARGE,
    EXTRA_LARGE
}