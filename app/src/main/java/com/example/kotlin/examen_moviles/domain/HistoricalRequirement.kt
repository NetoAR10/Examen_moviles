package com.example.kotlin.examen_moviles.domain

import android.util.Log
import com.example.kotlin.examen.data.repositories.HistoricalRepository
import com.example.kotlin.examen_moviles.data.network.model.Historical

/**
 * Caso de uso que encapsula la lógica para consultar datos históricos.
 * Actúa como intermediario entre la capa de dominio y la capa de datos.
 */
class HistoricalRequirement {

    // Instancia del repositorio encargado de manejar el acceso a los datos históricos.
    private val historicalRepository = HistoricalRepository()

    /**
     * Invoca el caso de uso para consultar datos históricos.
     *
     * @param callback Función que se ejecutará al completar la consulta. Recibe:
     *                 - Una lista de objetos `Historical` en caso de éxito.
     *                 - `null` si ocurre un error en la consulta.
     */
    operator fun invoke(callback: (List<Historical>?) -> Unit) {
        Log.d("HistoricalRequirement", "Consultando datos en el repositorio...")

        // Llama al método de consulta en el repositorio
        historicalRepository.consultarHistorical { historical ->
            Log.d("HistoricalRequirement", "Datos recibidos del repositorio: $historical")
            callback(historical) // Devuelve los datos al callback
        }
    }
}
