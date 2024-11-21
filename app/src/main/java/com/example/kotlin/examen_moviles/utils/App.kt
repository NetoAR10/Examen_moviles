package com.example.kotlin.examen_moviles.utils

import android.app.Application
import com.example.kotlin.examen_moviles.data.network.NetworkModuleDI

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkModuleDI.initializeParse(this)
    }
}