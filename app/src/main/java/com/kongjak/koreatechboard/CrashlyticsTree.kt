package com.kongjak.koreatechboard

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject
import javax.inject.Singleton
import timber.log.Timber

@Singleton
class CrashlyticsTree : Timber.Tree() {
    @Inject
    lateinit var crashlytics: FirebaseCrashlytics

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        when (priority) {
            Log.VERBOSE, Log.DEBUG, Log.INFO, Log.WARN -> return
            Log.ERROR, Log.ASSERT -> crashlytics.recordException(t ?: Exception(message))
        }
    }
}
