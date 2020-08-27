package ink.z31.catbooru.util

import android.app.Application
import android.content.Context

class AppUtil: Application() {
    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}