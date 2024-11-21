package com.example.kotlin.examen_moviles.framework.views.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kotlin.examen_moviles.databinding.ActivityHistoricalBinding
import com.example.kotlin.examen_moviles.framework.adapter.HistoricalAdapter
import com.example.kotlin.examen_moviles.framework.viewmodel.HistoricalViewModel

/**
 * Activity que muestra una lista de posturas en un RecyclerView con la capacidad de realizar búsquedas.
 * Utiliza un ViewModel para gestionar los datos y un adaptador para mostrar los elementos.
 */
class HistoricalActivity : AppCompatActivity() {
    // Enlace de vista generado para el diseño de la actividad (ActivityPosturasBinding)
    private lateinit var binding: ActivityHistoricalBinding

    // ViewModel para manejar los datos y la lógica de negocio de las posturas
    private val viewModel: HistoricalViewModel by viewModels()

    // Adaptador para el RecyclerView que muestra las posturas
    private lateinit var historicalAdapter: HistoricalAdapter

    /**
     * Método que se llama cuando se crea la actividad. Configura el enlace de vista,
     * inicializa el RecyclerView, activa el buscador y observa los datos del ViewModel.
     *
     * @param savedInstanceState Estado de la actividad guardado anteriormente.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inicializarVinculo()
        inicializarRecyclerView()
        eschucharBuscador()
        inicializarListeners()

        viewModel.consultarHistorical()
        observarViewModel()

    }

    /**
     * Inicializa el enlace de vista inflando el diseño y lo establece como el contenido de la actividad.
     */
    private fun inicializarVinculo() {
        binding = ActivityHistoricalBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    /**
     * Configura el RecyclerView con un adaptador y un GridLayoutManager de tres columnas.
     * Permite mostrar las posturas en un diseño de cuadrícula.
     */
    private fun inicializarRecyclerView() {
        historicalAdapter = HistoricalAdapter()
        binding.recyclerViewHistorical.layoutManager = GridLayoutManager(this, 1)
        binding.recyclerViewHistorical.adapter = historicalAdapter
    }

    /**
     * Observa los datos del ViewModel. Cuando cambian, actualiza los datos del adaptador
     * y verifica si hay resultados para mostrar u ocultar el estado "Sin Resultados".
     */
    private fun observarViewModel() {
        viewModel.historicalLiveData.observe(this) { historical ->
            Log.d("HistoricalActivity", "Datos recibidos: $historical")
            historicalAdapter.establecerHistoricalData(historical, this)
            verificarEstadoResultados(historical.isEmpty())
        }
    }

    /**
     * Inicializa los listeners para la actividad, incluyendo el botón de retroceso.
     * Al presionar el botón, se cierra la actividad actual.
     */
    private fun inicializarListeners() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    /**
     * Escucha el campo de búsqueda y filtra las posturas en tiempo real a medida
     * que el usuario escribe. Muestra u oculta el mensaje "Sin Resultados" si es necesario.
     */
    private fun eschucharBuscador() {
        binding.buscadorHistorical.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {}

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int,
                ) {
                    historicalAdapter.filtro(s.toString())
                    verificarEstadoResultados(historicalAdapter.itemCount == 0)
                }

                override fun afterTextChanged(s: Editable?) {}
            },
        )
    }
    /**
     * Verifica si se deben mostrar u ocultar los elementos de "Sin Resultados" según el estado de la lista.
     *
     * @param sinResultados Indica si no se encontraron posturas en la lista.
     */
    private fun verificarEstadoResultados(sinResultados: Boolean) {
        binding.contenedorSinResultados.visibility = if (sinResultados) View.VISIBLE else View.GONE
        binding.recyclerViewHistorical.visibility = if (sinResultados) View.GONE else View.VISIBLE
    }

}