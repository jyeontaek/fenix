/* This Source Code Form is subject to the terms of the Mozilla Public
   License, v. 2.0. If a copy of the MPL was not distributed with this
   file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import mozilla.components.browser.session.tab.CustomTabConfig
import mozilla.components.support.utils.SafeIntent
import org.mozilla.fenix.components.NotificationManager.Companion.RECEIVE_TABS_TAG
import org.mozilla.fenix.customtabs.CustomTabActivity
import org.mozilla.fenix.ext.components
import org.mozilla.fenix.utils.Settings

private const val SPEECH_REQUEST_CODE = 0

class IntentReceiverActivity : Activity() {

    private var previousIntent : Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.search_widget)

        // The intent property is nullable, but the rest of the code below
        // assumes it is not. If it's null, then we make a new one and open
        // the HomeActivity.
        val intent = intent?.let { Intent(intent) } ?: Intent()

        val isPrivate = Settings.getInstance(this).usePrivateMode

        if (isPrivate) {
            components.utils.privateIntentProcessor.process(intent)
        } else {
            components.utils.intentProcessor.process(intent)
        }

        val openToBrowser = when {
            CustomTabConfig.isCustomTabIntent(SafeIntent(intent)) -> {
                intent.setClassName(applicationContext, CustomTabActivity::class.java.name)
                true
            }
            intent.action == Intent.ACTION_VIEW -> {
                intent.setClassName(applicationContext, HomeActivity::class.java.name)
                if (!intent.getBooleanExtra(RECEIVE_TABS_TAG, false)) {
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                true
            }
            else -> {
                intent.setClassName(applicationContext, HomeActivity::class.java.name)
                false
            }
        }

        //val openToSearch = intent.getBooleanExtra(HomeActivity.OPEN_TO_SEARCH, false)

        intent.putExtra(HomeActivity.OPEN_TO_BROWSER, openToBrowser)
        //intent.putExtra(HomeActivity.OPEN_TO_SEARCH, openToSearch)

        if (intent.getBooleanExtra(SPEECH_PROCESSING, false)) {
            Log.d("pendingIntent", "Entered speech recognizer")
            previousIntent = intent
            displaySpeechRecognizer()
        } else {
            startActivity(intent)
            finish()
        }
    }

    override fun onPause() {
        Log.d("pendingIntent", "onPause")

        super.onPause()
    }

    override fun onResume() {
        Log.d("pendingIntent", "onResume")

        super.onResume()
    }

    override fun onDestroy() {
        Log.d("pendingIntent", "onDestroy")
        super.onDestroy()
    }

    private fun displaySpeechRecognizer() {
        val intentSpeech = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        }

        Log.d("pendingIntent", "Entered speech recognizer 2")
        startActivityForResult(intentSpeech, SPEECH_REQUEST_CODE)
        Log.d("pendingIntent", "Entered speech recognizer 4")

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("pendingIntent", "Entered speech recognizer 3")

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val spokenText: String? =
                    data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.let { results ->
                        results[0]
                    }
            Log.d("pendingIntent", spokenText)
        } else {
            finish()
        }

        startActivity(previousIntent)
        Log.d("pendingIntent", "did not crash")
        super.onActivityResult(requestCode, resultCode, data)
        finish()

    }

    companion object {
        const val SPEECH_PROCESSING = "speech_processing"
        const val INTENT_PASS = "intent_pass"
    }
}
