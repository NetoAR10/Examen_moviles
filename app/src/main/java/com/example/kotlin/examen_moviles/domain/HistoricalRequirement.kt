package com.example.kotlin.examen_moviles.domain

import android.util.Log
import com.example.kotlin.examen.data.repositories.HistoricalRepository
import com.example.kotlin.examen_moviles.data.network.model.Historical

class HistoricalRequirement {

    // Instancia del repositorio para realizar consultas de posturas.
    private val historicalRepository = HistoricalRepository()

    operator fun invoke(callback: (List<Historical>?) -> Unit) {// Llama al método de consulta en el repositorio y pasa el resultado al callback.
        Log.d("HistoricalRequirement", "Consultando datos en el repositorio...")
        historicalRepository.consultarHistorical { historical ->
            Log.d("HistoricalRequirement", "Datos recibidos del repositorio: $historical")
            callback(historical)
        }
    }
}