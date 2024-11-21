package com.example.kotlin.examen_moviles.framework.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.examen.framework.adapter.viewholder.HistoricalViewHolder
import com.example.kotlin.examen_moviles.data.network.model.Historical
import com.example.kotlin.examen_moviles.databinding.ItemHistoricalBinding

/**
 * Adaptador para gestionar los elementos de datos históricos (`Historical`) en un RecyclerView.
 * Proporciona funcionalidades como filtrado y configuración de los datos.
 */
class HistoricalAdapter() : RecyclerView.Adapter<HistoricalViewHolder>() {

    // Lista de datos original (sin filtrar)
    private var datos: List<Historical> = emptyList()

    // Lista para almacenar los datos filtrados
    private var datosFiltrados: List<Historical> = emptyList()

    // Contexto de la actividad actual
    private lateinit var contexto: Context

    /**
     * Establece la lista inicial de datos históricos y actualiza el adaptador.
     *
     * @param basicData Lista de datos históricos (`Historical`) a mostrar.
     * @param contexto Contexto de la actividad actual.
     */
    fun establecerHistoricalData(
        basicData: List<Historical>,
        contexto: Context,
    ) {
        Log.d("HistoricalAdapter", "Datos establecidos: $basicData")
        this.datos = basicData
        this.datosFiltrados = basicData // Al principio, no hay filtro, por lo que se muestra toda la lista
        this.contexto = contexto
        notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado
    }

    /**
     * Filtra los datos históricos según una consulta de texto.
     *
     * @param query Texto utilizado para filtrar las descripciones de los datos históricos.
     */
    fun filtro(query: String) {
        datosFiltrados =
            if (query.isEmpty()) {
                datos // Si la consulta está vacía, muestra todos los datos
            } else {
                datos.filter { it.description.contains(query, ignoreCase = true) } // Filtra por descripción, ignorando mayúsculas/minúsculas
            }
        notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado
    }

    /**
     * Aplica filtros basados en los valores de `category1` y `category2`.
     *
     * @param category1 Valor seleccionado para filtrar por la primera categoría.
     * @param category2 Valor seleccionado para filtrar por la segunda categoría.
     */
    fun aplicarFiltro(category1: String, category2: String) {
        Log.d("HistoricalAdapter", "Datos antes de filtrar: $datos")
        datosFiltrados = datos.filter {
            (category1 == "Todos" || it.category1 == category1) &&
                    (category2 == "Todos" || it.category2 == category2)
        }
        Log.d("HistoricalAdapter", "Datos filtrados: $datosFiltrados")
        notifyDataSetChanged()
    }

    /**
     * Obtiene una lista de valores únicos para un campo específico de los datos históricos.
     *
     * @param campo Nombre del campo (`category1` o `category2`).
     * @return Lista de valores únicos, incluyendo "Todos" como la primera opción.
     */
    fun obtenerUnicos(campo: String): List<String> {
        val valores = when (campo) {
            "category1" -> datos.mapNotNull { it.category1 }
            "category2" -> datos.mapNotNull { it.category2 }
            else -> emptyList()
        }
        val unicos = listOf("Todos") + valores.distinct()
        Log.d("HistoricalAdapter", "Valores únicos para $campo: $unicos")
        return unicos
    }

    /**
     * Crea un nuevo ViewHolder para los elementos del RecyclerView.
     *
     * @param parent Vista padre que contiene los elementos.
     * @param viewType Tipo de vista para el elemento (no utilizado en este caso).
     * @return Instancia de `HistoricalViewHolder`.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoricalViewHolder {
        val binding = ItemHistoricalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoricalViewHolder(binding)
    }

    /**
     * Vincula un elemento de datos históricos a un ViewHolder.
     *
     * @param holder ViewHolder que manejará el elemento.
     * @param position Posición del elemento en la lista filtrada.
     */
    override fun onBindViewHolder(holder: HistoricalViewHolder, position: Int) {
        val item = datosFiltrados[position]
        holder.bind(item, contexto as FragmentActivity)
    }

    /**
     * Devuelve el número de elementos en la lista filtrada.
     *
     * @return Cantidad de elementos actualmente visibles en el RecyclerView.
     */
    override fun getItemCount(): Int {
        return datosFiltrados.size
    }
}
