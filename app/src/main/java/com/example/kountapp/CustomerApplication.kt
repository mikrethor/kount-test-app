package com.example.kountapp

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.kount.api.analytics.AnalyticsApplication
import com.kount.api.analytics.enums.EventEnums

class CustomerApplication: Application(), Application.ActivityLifecycleCallbacks {
    override fun onCreate() {
        AnalyticsApplication.init()
        registerActivityLifecycleCallbacks(this)
        super.onCreate()
    }

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
        AnalyticsApplication.registerKountEvents(EventEnums.EVENT_STARTED, activity)
    }
    override fun onActivityResumed(activity: Activity) {
        AnalyticsApplication.registerKountEvents(EventEnums.EVENT_RESUMED, activity)
    }
    override fun onActivityPaused(activity: Activity) {
        AnalyticsApplication.registerKountEvents(EventEnums.EVENT_PAUSED, activity)
    }
    override fun onActivityStopped(activity: Activity) {
        AnalyticsApplication.registerKountEvents(EventEnums.EVENT_STOPPED, activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {}
}