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

/**
 * Objeto responsable de manejar las configuraciones de red y las llamadas a funciones en la nube de Parse.
 */
object NetworkModuleDI {

    // Cliente HTTP para realizar solicitudes
    private val client = OkHttpClient()

    /**
     * Inicializa Parse con la configuración necesaria para la conexión al backend.
     *
     * @param context Contexto de la aplicación utilizado para inicializar Parse.
     */
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
     * Llama a una función en la nube de Parse de forma personalizada utilizando OkHttp.
     *
     * @param nombreFuncion Nombre de la función en la nube que se va a ejecutar.
     * @param parametros Mapa de parámetros que se pasarán a la función en la nube.
     * @param maxRetries Número máximo de reintentos en caso de fallo.
     * @param callback Callback que se llamará con el resultado o el error.
     *                 - Si la llamada es exitosa, recibe un `JSONObject` como primer argumento y `null` como segundo.
     *                 - Si ocurre un error, recibe `null` como primer argumento y una `Exception` como segundo.
     */
    fun callCloudFunction(
        nombreFuncion: String,
        parametros: HashMap<String, Any>,
        maxRetries: Int = 3,
        callback: (JSONObject?, Exception?) -> Unit
    ) {
        val url = "$BASE_URL/$nombreFuncion"

        // Crear el cuerpo de la solicitud en formato JSON
        val jsonBody = JSONObject(parametros as Map<String, Any>).toString()
        val requestBody = jsonBody.toRequestBody("application/json; charset=utf-8".toMediaType())

        // Construir la solicitud HTTP con los encabezados requeridos
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .addHeader("X-Parse-Application-Id", APPLICATION_ID)
            .addHeader("X-Parse-REST-API-Key", REST_API_KEY)
            .build()

        /**
         * Ejecuta la solicitud HTTP con reintentos en caso de fallo.
         *
         * @param retryCount Número actual de intentos realizados.
         */
        fun executeRequest(retryCount: Int) {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    if (retryCount < maxRetries) {
                        Log.w("NetworkModuleDI", "Fallo en el intento $retryCount, reintentando...")
                        executeRequest(retryCount + 1) // Reintentar si no se ha alcanzado el límite
                    } else {
                        Log.e("NetworkModuleDI", "Error tras $maxRetries intentos: ${e.message}")
                        callback(null, e) // Devolver el error al callback
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        val json = JSONObject(responseBody ?: "")
                        callback(json, null) // Devolver el resultado al callback
                    } else {
                        if (retryCount < maxRetries) {
                            Log.w("NetworkModuleDI", "Error HTTP ${response.code} en el intento $retryCount, reintentando...")
                            executeRequest(retryCount + 1) // Reintentar en caso de error HTTP
                        } else {
                            Log.e("NetworkModuleDI", "Error HTTP tras $maxRetries intentos: ${response.code}")
                            callback(null, Exception("Error HTTP: ${response.code}")) // Devolver el error al callback
                        }
                    }
                }
            })
        }

        // Inicia la solicitud con el primer intento
        executeRequest(0)
    }
}

