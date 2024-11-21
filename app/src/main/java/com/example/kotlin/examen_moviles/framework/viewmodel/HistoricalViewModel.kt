package com.example.kotlin.examen_moviles.framework.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlin.examen_moviles.data.network.model.Historical
import com.example.kotlin.examen_moviles.domain.HistoricalRequirement

/**
 * ViewModel encargado de gestionar la lógica de negocio y los datos para la vista que muestra los elementos históricos.
 */
class HistoricalViewModel : ViewModel() {

    // LiveData privada para almacenar los datos históricos, accesible solo dentro de esta clase.
    private val _historicalLiveData = MutableLiveData<List<Historical>>()

    /**
     * LiveData pública que expone los datos históricos a la vista para ser observados,
     * sin permitir modificaciones desde fuera del ViewModel.
     */
    val historicalLiveData: LiveData<List<Historical>> get() = _historicalLiveData

    // Dependencia que encapsula la lógica de consulta de datos históricos.
    private val historicalRequirement = HistoricalRequirement()

    /**
     * Realiza la consulta de datos históricos utilizando el caso de uso `HistoricalRequirement`.
     * Los resultados se publican en el LiveData para ser observados por la vista.
     */
    fun consultarHistorical() {
        Log.d("HistoricalViewModel", "Consultando datos históricos...")

        // Invoca el caso de uso para obtener los datos
        historicalRequirement { result ->
            Log.d("HistoricalViewModel", "Datos obtenidos: $result")

            // Publica los resultados en el LiveData (lista vacía si hay un error o no hay datos)
            _historicalLiveData.postValue(result ?: emptyList())
        }
    }
}
