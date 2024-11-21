package com.example.kotlin.examen_moviles.framework.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kotlin.examen_moviles.databinding.FragmentHistoricalDetailBinding

/**
 * Fragment que muestra los detalles de un elemento histórico, incluyendo su descripción
 * y fecha en un diseño centrado y scrolleable.
 */
class HistoricalDetailFragment : Fragment() {

    // Variable para gestionar el binding de la vista
    private var _binding: FragmentHistoricalDetailBinding? = null
    private val binding get() = _binding!!

    /**
     * Infla el diseño del fragmento.
     *
     * @param inflater Objeto utilizado para inflar la vista del fragmento.
     * @param container Contenedor padre del fragmento.
     * @param savedInstanceState Estado guardado previamente del fragmento.
     * @return Vista inflada del fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoricalDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Configura la lógica del fragmento después de que la vista ha sido creada.
     *
     * @param view Vista inflada del fragmento.
     * @param savedInstanceState Estado guardado previamente del fragmento.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtén los argumentos pasados al Fragment
        val description = arguments?.getString("description") ?: "No description available"
        val rawDate = arguments?.getString("date") ?: "Unknown date"

        // Procesar la fecha para mostrarla sin el signo "-" y añadir " B.C"
        val formattedDate = if (rawDate.startsWith("-")) {
            rawDate.removePrefix("-") + " B.C"
        } else {
            rawDate // Mantenerla igual si no empieza con "-"
        }

        // Asigna los valores a las vistas
        binding.descriptionTextView.text = description
        binding.dateTextView.text = formattedDate

        // Configura el botón de cerrar
        binding.closeButton.setOnClickListener {
            parentFragmentManager.popBackStack() // Cierra el fragmento
        }
    }

    /**
     * Limpia el binding cuando la vista se destruye para evitar fugas de memoria.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
