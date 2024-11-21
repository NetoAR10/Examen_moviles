package com.example.kotlin.examen_moviles.framework.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlin.examen_moviles.data.network.model.Historical
import com.example.kotlin.examen_moviles.domain.HistoricalRequirement

class HistoricalViewModel : ViewModel() {

    // LiveData privada para almacenar las posturas, accesible solo dentro de esta clase.
    private val _historicalLiveData = MutableLiveData<List<Historical>>()

    // LiveData pública para ser observada externamente sin exponer su capacidad de modificación.
    val historicalLiveData: LiveData<List<Historical>> get() = _historicalLiveData

    // Dependencia que permite la consulta de las posturas.
    private val HistoricalRequirement = HistoricalRequirement()

    fun consultarHistorical() {
        Log.d("HistoricalViewModel", "Consultando datos históricos...")
        HistoricalRequirement { result ->
            Log.d("HistoricalViewModel", "Datos obtenidos: $result")
            _historicalLiveData.postValue(
                result ?: emptyList()
            ) // Actualiza LiveData con el resultado o una lista vacía}}
        }
    }
}