package com.example.kotlin.examen_moviles.data.network

import android.content.Context
import android.util.Log
import com.example.kotlin.examen_moviles.utils.Constants.APPLICATION_ID
import com.example.kotlin.examen_moviles.utils.Constants.BASE_URL
import com.example.kotlin.examen_moviles.utils.Constants.REST_API_KEY
import com.parse.Parse
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

object NetworkModuleDI {

    private val client = OkHttpClient()


    fun initializeParse(context: Context) {
        Parse.initialize(
            Parse.Configuration.Builder(context)
                .applicationId(APPLICATION_ID)
                .clientKey(REST_API_KEY)
                .server(BASE_URL)
                .build()
        )
    }

    /**
     * Llama a una función en la nube de Parse de forma personalizada con OkHttp.
     *
     * @param nombreFuncion El nombre de la función en la nube que se va a ejecutar.
     * @param parametros Un mapa de parámetros que se pasarán a la función en la nube.
     * @param callback La función que se llamará con el resultado o el error.
     */
    fun callCloudFunction(
        nombreFuncion: String,
        parametros: HashMap<String, Any>,
        maxRetries: Int = 3, // Máximo número de reintentos
        callback: (JSONObject?, Exception?) -> Unit
    ) {
        val url = "$BASE_URL/$nombreFuncion"

        // Crear el cuerpo de la solicitud
        val jsonBody = JSONObject(parametros as Map<String, Any>).toString()
        val requestBody = jsonBody.toRequestBody("application/json; charset=utf-8".toMediaType())


        // Construir la solicitud con los encabezados requeridos
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .addHeader("X-Parse-Application-Id", APPLICATION_ID)
            .addHeader("X-Parse-REST-API-Key", REST_API_KEY)
            .build()

        // Función para manejar reintentos
        fun executeRequest(retryCount: Int) {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    if (retryCount < maxRetries) {
                        executeRequest(retryCount + 1) // Reintentar en caso de fallo
                    } else {
                        callback(null, e)
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        val json = JSONObject(responseBody ?: "")
                        callback(json, null)
                    } else {
                        if (retryCount < maxRetries) {
                            executeRequest(retryCount + 1) // Reintentar en caso de error HTTP
                        } else {
                            callback(null, Exception("Error HTTP: ${response.code}"))
                        }
                    }
                }
            })
        }

        // Iniciar la solicitud con el primer intento
        executeRequest(0)
    }
}
