package com.example.kotlin.examen_moviles.utils

import android.app.Application
import com.example.kotlin.examen_moviles.data.network.NetworkModuleDI

/**
 * Clase `Application` personalizada que se utiliza para inicializar configuraciones
 * globales de la aplicación, como la inicialización de Parse.
 */
class App : Application() {

    /**
     * Método llamado cuando la aplicación se crea por primera vez.
     * Aquí se realiza la inicialización de configuraciones globales.
     */
    override fun onCreate() {
        super.onCreate()

        // Inicializar Parse al iniciar la aplicación
        NetworkModuleDI.initializeParse(this)
    }
}
