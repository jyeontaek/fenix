package org.mozilla.fenix

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetManager.*
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews

class SearchWidgetProvider : AppWidgetProvider() {

    companion object {
        private val DP_EXTRA_SMALL = 64
        private val DP_SMALL = 100
        private val DP_MEDIUM = 192
        private val DP_LARGE = 256
        private val DP_EXTRA_LARGE = 360
        private val REQUEST_CODE_NEW_TAB = 0
        private val REQUEST_CODE_VOICE = 1
    }

    private fun getLayout(dp: Int) : Int {
        if (dp >= DP_EXTRA_LARGE) {
            Log.d("pendingIntent", "Hi 1")
            return R.layout.search_widget_extra_large

        } else if (dp >= DP_LARGE) {
            Log.d("pendingIntent", "Hi 2")
            return R.layout.search_widget_large

        } else if (dp >= DP_MEDIUM) {
            Log.d("pendingIntent", "Hi 3")
            return R.layout.search_widget_medium
        } else if (dp >= DP_SMALL) {
            Log.d("pendingIntent", "Hi 4")
            return R.layout.search_widget_small

        } else {
            Log.d("pendingIntent", "Hi 5")
            return R.layout.search_widget_extra_small

        }
        /*
        if (dp <= DP_EXTRA_SMALL) {
            Log.d("pendingIntent", "Hi 1")
            return R.layout.search_widget_extra_small
        } else if (dp <= DP_SMALL) {
            Log.d("pendingIntent", "Hi 2")
            return R.layout.search_widget_small
        } else if (dp <= DP_MEDIUM) {
            Log.d("pendingIntent", "Hi 3")
            return R.layout.search_widget_medium
        } else if (dp <= DP_LARGE) {
            Log.d("pendingIntent", "Hi 4")
            return R.layout.search_widget_large
        } else {
            Log.d("pendingIntent", "Hi 5")
            return R.layout.search_widget_extra_large
        }

         */
    }

    private fun getText(layout: Int, context: Context) = when (layout) {
        R.layout.search_widget_medium -> context.getString(R.string.search_widget_short)
        R.layout.search_widget_large,
        R.layout.search_widget_extra_large -> context.getString(R.string.search_widget_long)
        else -> null
    }

    private fun createTextSearchIntent(context: Context) : PendingIntent {
        return Intent(context, HomeActivity::class.java)
            .let { intent ->
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra(HomeActivity.OPEN_TO_SEARCH, true)
                PendingIntent.getActivity(context, REQUEST_CODE_NEW_TAB, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            }
    }

    private fun createVoiceSearchIntent(context: Context) : PendingIntent {
        return Intent(context, IntentReceiverActivity::class.java)
            .let { intent ->
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra(IntentReceiverActivity.SPEECH_PROCESSING, true)
                PendingIntent.getActivity(context, REQUEST_CODE_VOICE, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val textSearchIntent = createTextSearchIntent(context)
        val voiceSearchIntent = createVoiceSearchIntent(context)

        appWidgetIds.forEach { appWidgetId ->
            val currentWidth = appWidgetManager.getAppWidgetOptions(appWidgetId).getInt(OPTION_APPWIDGET_MIN_WIDTH)
            val layout = getLayout(currentWidth)
            val text = getText(layout, context)
            Log.d("pendingIntent", "dp1: " + currentWidth.toString())
            Log.d("pendingIntent", "layout1: " + layout.toString())

            val views: RemoteViews = RemoteViews(context.packageName, layout).apply {
                when (layout) {
                    R.layout.search_widget_extra_small -> {
                        setOnClickPendingIntent(R.id.button_search_widget_new_tab, textSearchIntent)
                    }
                    R.layout.search_widget_small -> {
                        setOnClickPendingIntent(R.id.button_search_widget_new_tab, textSearchIntent)
                        setOnClickPendingIntent(R.id.button_search_widget_voice, voiceSearchIntent)
                    }
                    R.layout.search_widget_medium,
                    R.layout.search_widget_large,
                    R.layout.search_widget_extra_large -> {
                        setOnClickPendingIntent(R.id.button_search_widget_new_tab, textSearchIntent)
                        setOnClickPendingIntent(R.id.button_search_widget_voice, voiceSearchIntent)
                        setTextViewText(R.id.text_search_widget, text)
                    }
                }
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        val textSearchIntent = createTextSearchIntent(context)
        val voiceSearchIntent = createVoiceSearchIntent(context)

        val currentWidth = appWidgetManager.getAppWidgetOptions(appWidgetId).getInt(OPTION_APPWIDGET_MIN_WIDTH)
        Log.d("pendingIntent", "dp2: " + currentWidth.toString())
        val layout = getLayout(currentWidth)
        Log.d("pendingIntent", "layout2: " + layout.toString())
        appWidgetManager.getAppWidgetInfo(appWidgetId).initialLayout = layout

        val text = getText(layout, context)
        val views: RemoteViews = RemoteViews(context.packageName, layout).apply {
            when (layout) {
                R.layout.search_widget_extra_small -> {
                    setOnClickPendingIntent(R.id.button_search_widget_new_tab, textSearchIntent)
                }
                R.layout.search_widget_small -> {
                    setOnClickPendingIntent(R.id.button_search_widget_new_tab, textSearchIntent)
                    setOnClickPendingIntent(R.id.button_search_widget_voice, voiceSearchIntent)
                }
                R.layout.search_widget_medium,
                R.layout.search_widget_large,
                R.layout.search_widget_extra_large -> {
                    setOnClickPendingIntent(R.id.button_search_widget_new_tab, textSearchIntent)
                    setOnClickPendingIntent(R.id.button_search_widget_voice, voiceSearchIntent)
                    setTextViewText(R.id.text_search_widget, text)
                }
            }
        }
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }


}

/*
class SearchWidgetOneButtonProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {

        val pendingIntent: PendingIntent = Intent(context, HomeActivity::class.java)
            .let { intent ->
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra(HomeActivity.OPEN_TO_SEARCH, true)
                PendingIntent.getActivity(context, 0, intent, 0)
            }

        val views: RemoteViews = RemoteViews(context.packageName, R.layout.search_widget_extra_small)
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

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        appWidgetManager.let {
            val dp = it.getAppWidgetOptions(appWidgetId).getInt(OPTION_APPWIDGET_MIN_WIDTH)
            Log.d("pendingIntent", "Max:" + dp.toString())
            if (dp >= 360.0) {
                val views: RemoteViews = RemoteViews(context.packageName, R.layout.search_widget_extra_large)
                appWidgetManager.updateAppWidget(appWidgetId, views)
            } else if (dp >= 256.0) {
                val views: RemoteViews = RemoteViews(context.packageName, R.layout.search_widget_large)
                appWidgetManager.updateAppWidget(appWidgetId, views)
            } else if (dp >= 192.0) {
                val views: RemoteViews = RemoteViews(context.packageName, R.layout.search_widget_medium)
                appWidgetManager.updateAppWidget(appWidgetId, views)
            } else if (dp >= 100.0) {
                val views: RemoteViews = RemoteViews(context.packageName, R.layout.search_widget_small)
                appWidgetManager.updateAppWidget(appWidgetId, views)
            } else if (dp >= 64.0) {
                val views: RemoteViews = RemoteViews(context.packageName, R.layout.search_widget_extra_small)
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
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
*/