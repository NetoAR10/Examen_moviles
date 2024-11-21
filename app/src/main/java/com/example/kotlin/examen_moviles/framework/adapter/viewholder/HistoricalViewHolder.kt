package com.example.kotlin.examen.framework.adapter.viewholder

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.examen_moviles.data.network.model.Historical
import com.example.kotlin.examen_moviles.databinding.ItemHistoricalBinding
import com.example.kotlin.examen_moviles.framework.views.fragments.HistoricalDetailFragment

/**
 * ViewHolder para gestionar la visualización y eventos de cada elemento en el RecyclerView
 * que muestra los datos históricos (`Historical`).
 *
 * @param binding Enlace generado para el diseño del elemento (ItemHistoricalBinding).
 */
class HistoricalViewHolder(
    private val binding: ItemHistoricalBinding,
) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Vincula un objeto `Historical` con el diseño del elemento.
     * También configura el evento de clic para abrir un detalle ampliado del elemento.
     *
     * @param historical Objeto `Historical` con los datos a mostrar en la tarjeta.
     * @param context Contexto de la actividad actual, utilizado para manejar la navegación de fragmentos.
     */
    fun bind(historical: Historical, context: FragmentActivity) {
        // Asigna la descripción al TextView correspondiente
        binding.descripcionTextView.text = historical.description

        // Configura el evento de clic en la tarjeta
        itemView.setOnClickListener {
            // Crear un Fragment para mostrar los detalles del elemento
            val fragment = HistoricalDetailFragment().apply {
                // Pasar los datos al Fragment mediante un Bundle
                arguments = Bundle().apply {
                    putString("description", historical.description) // Descripción del elemento
                    putString("date", historical.date) // Fecha del elemento
                }
            }

            // Reemplazar el Fragment actual con el nuevo Fragment
            context.supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment) // Reemplaza el contenido del contenedor
                .addToBackStack(null) // Agrega el Fragment a la pila de retroceso
                .commit()
        }
    }
}
