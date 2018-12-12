package com.contactlist.main

import android.app.Application


class App: Application() {

    private var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()

        appComponent!!.inject(this)
    }

    fun getAppComponent() : AppComponent?{

        return appComponent
    }
}

