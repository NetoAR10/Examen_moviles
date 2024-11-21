package com.example.kotlin.examen.data.network

import android.content.Context
import com.example.kotlin.examen.utils.Constants.APPLICATION_ID
import com.example.kotlin.examen.utils.Constants.BASE_URL
import com.example.kotlin.examen.utils.Constants.CLIENT_KEY
import com.parse.Parse
import com.parse.ParseCloud
import com.parse.ParseException

/**
 * Módulo de inyección de dependencias responsable de inicializar Parse y
 * manejar las llamadas a las funciones en la nube de Parse.
 */
object NetworkModuleDI {
    /**
     * Inicializa Parse con la configuración necesaria para la conexión al backend.
     *
     * @param context El contexto de la aplicación utilizado para la configuración de Parse.
     */
    fun initializeParse(context: Context) {
        Parse.initialize(
            Parse.Configuration
                .Builder(context)
                .applicationId(APPLICATION_ID)
                .clientKey(CLIENT_KEY)
                .server(BASE_URL)
                .build(),
        )
    }

    /**
     * Llama a una función en la nube de Parse de forma asíncrona y maneja el resultado o error.
     *
     * @param T El tipo de resultado esperado de la función en la nube.
     * @param nombreFuncion El nombre de la función en la nube que se va a ejecutar.
     * @param parametros Un mapa de parámetros que se pasarán a la función en la nube.
     * @param callback La función que se llamará con el resultado o el error. Si la llamada es exitosa,
     * [callback] recibirá el resultado como el primer argumento y `null` como segundo argumento.
     * En caso de error, recibirá `null` como primer argumento y una excepción como segundo argumento.
     */
    fun <T> callCloudFunction(
        nombreFuncion: String,
        parametros: HashMap<String, Any>,
        callback: (T?, Exception?) -> Unit,
    ) {
        ParseCloud.callFunctionInBackground(nombreFuncion, parametros) { resultado: T?, e: ParseException? ->
            if (e == null) {
                callback(resultado, null)
            } else {
                callback(null, e)
            }
        }
    }
}