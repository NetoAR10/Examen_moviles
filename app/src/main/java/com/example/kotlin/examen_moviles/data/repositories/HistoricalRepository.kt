package com.example.kotlin.examen.data.repositories

import android.util.Log
import com.example.kotlin.examen_moviles.data.network.NetworkModuleDI
import com.example.kotlin.examen_moviles.data.network.model.Historical
import com.google.gson.Gson
import com.parse.ParseException

/**
 * Repositorio que maneja la lógica de acceso a datos para las posturas.
 */
class HistoricalRepository {

    fun consultarHistorical(callback: (List<Historical>?) -> Unit) {
        val parametros = hashMapOf<String, Any>("page" to 1)


        NetworkModuleDI.callCloudFunction(
            nombreFuncion = "hello",
            parametros = parametros,
            maxRetries = 3
        ) { resultado, e ->
            if (e == null) {
                try {
                    // Extraer el array "data" del campo "result"
                    val data = resultado?.getJSONObject("result")?.getJSONArray("data")
                    val gson = Gson()

                    // Convertir cada elemento del array a un objeto `Historical`
                    val historicalList = mutableListOf<Historical>()
                    for (i in 0 until (data?.length() ?: 0)) {
                        val jsonObject = data?.getJSONObject(i)
                        jsonObject?.let {
                            val historical = gson.fromJson(it.toString(), Historical::class.java)
                            historicalList.add(historical)
                        }
                    }

                    Log.d("HistoricalRepository", "Datos mapeados: $historicalList")
                    callback(historicalList) // Devuelve la lista al ViewModel
                } catch (ex: Exception) {
                    Log.e("HistoricalRepository", "Error procesando la respuesta: ${ex.message}")
                    callback(null) // Devuelve null en caso de error de procesamiento
                }
            } else {
                Log.e("HistoricalRepository", "Error de conexión: ${e.message}")
                callback(null) // Devuelve null en caso de error de conexión
            }
        }
    }

}