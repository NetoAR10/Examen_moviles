package com.example.kotlin.examen.data.repositories

import android.util.Log
import com.example.kotlin.examen_moviles.data.network.NetworkModuleDI
import com.example.kotlin.examen_moviles.data.network.model.Historical
import com.google.gson.Gson

/**
 * Repositorio que maneja la lógica de acceso a datos para los registros históricos (`Historical`).
 */
class HistoricalRepository {

    /**
     * Consulta los datos históricos desde el backend y los convierte en una lista de objetos `Historical`.
     *
     * @param callback Función que se ejecutará al completar la consulta. Recibe:
     *                 - Una lista de objetos `Historical` en caso de éxito.
     *                 - `null` si ocurre un error.
     */
    fun consultarHistorical(callback: (List<Historical>?) -> Unit) {
        // Parámetros necesarios para la llamada a la función en la nube
        val parametros = hashMapOf<String, Any>("page" to 1)

        // Llama a la función en la nube utilizando el módulo de red
        NetworkModuleDI.callCloudFunction(
            nombreFuncion = "hello",
            parametros = parametros,
            maxRetries = 3 // Número máximo de reintentos en caso de fallo
        ) { resultado, e ->
            if (e == null) { // Si no hay error, procesa el resultado
                try {
                    // Extraer el array "data" del campo "result" en el JSON
                    val data = resultado?.getJSONObject("result")?.getJSONArray("data")
                    val gson = Gson()

                    // Convertir cada elemento del array JSON en un objeto `Historical`
                    val historicalList = mutableListOf<Historical>()
                    for (i in 0 until (data?.length() ?: 0)) {
                        val jsonObject = data?.getJSONObject(i)
                        jsonObject?.let {
                            val historical = gson.fromJson(it.toString(), Historical::class.java)
                            historicalList.add(historical)
                        }
                    }

                    // Log de los datos mapeados
                    Log.d("HistoricalRepository", "Datos mapeados: $historicalList")

                    // Devuelve la lista de objetos `Historical` al callback
                    callback(historicalList)
                } catch (ex: Exception) {
                    // Manejo de errores en el procesamiento del JSON
                    Log.e("HistoricalRepository", "Error procesando la respuesta: ${ex.message}")
                    callback(null) // Devuelve null en caso de error
                }
            } else {
                // Manejo de errores de conexión
                Log.e("HistoricalRepository", "Error de conexión: ${e.message}")
                callback(null) // Devuelve null si hay un error de conexión
            }
        }
    }
}
