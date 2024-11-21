package com.example.kotlin.examen.utils

import android.app.Application
import com.example.kotlin.examen.data.network.NetworkModuleDI

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkModuleDI.initializeParse(this)
    }
}