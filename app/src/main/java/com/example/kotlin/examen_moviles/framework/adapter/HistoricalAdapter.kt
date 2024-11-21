package com.example.kotlin.examen_moviles.framework.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.examen.framework.adapter.viewholder.HistoricalViewHolder
import com.example.kotlin.examen_moviles.data.network.model.Historical
import com.example.kotlin.examen_moviles.databinding.ItemHistoricalBinding

class HistoricalAdapter() : RecyclerView.Adapter<HistoricalViewHolder>() {

    // Lista de datos original (sin filtrar)
    private var datos: List<Historical> = emptyList()

    // Lista para almacenar los datos filtrados
    private var datosFiltrados: List<Historical> = emptyList()

    private lateinit var contexto: Context


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

    fun filtro(query: String) {
        datosFiltrados =
            if (query.isEmpty()) {
                datos // Si la consulta está vacía, muestra todos los datos
            } else {
                datos.filter { it.description.contains(query, ignoreCase = true) } // Filtra por nombre, ignorando mayúsculas/minúsculas
            }
        notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado
    }

    fun aplicarFiltro(category1: String, category2: String) {
        Log.d("HistoricalAdapter", "Datos antes de filtrar: $datos")
        datosFiltrados = datos.filter {
            (category1 == "Todos" || it.category1 == category1) &&
                    (category2 == "Todos" || it.category2 == category2)
        }
        Log.d("HistoricalAdapter", "Datos filtrados: $datosFiltrados")
        notifyDataSetChanged()
    }


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



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoricalViewHolder {
        val binding = ItemHistoricalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoricalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoricalViewHolder, position: Int) {
        val item = datosFiltrados[position]
        holder.bind(item, contexto) // Pasa el objeto postura y contexto al ViewHolder
    }


    override fun getItemCount(): Int {
        return datosFiltrados.size // Devuelve el tamaño de los datos filtrados
    }
}