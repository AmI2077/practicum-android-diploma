package ru.practicum.android.diploma.core.android

import android.app.Application
import org.koin.core.context.startKoin
import ru.practicum.android.diploma.core.di.appModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(appModule)
        }
    }
}
