package com.mobile.negocio

import android.app.Application
import com.mobile.negocio.data.AppContainer
import com.mobile.negocio.data.AppDataContainer

class NegocioApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}