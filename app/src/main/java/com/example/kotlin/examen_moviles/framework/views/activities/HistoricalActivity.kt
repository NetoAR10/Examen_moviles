package com.example.kotlin.examen_moviles.framework.views.activities

import android.R
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kotlin.examen_moviles.databinding.ActivityHistoricalBinding
import com.example.kotlin.examen_moviles.framework.adapter.HistoricalAdapter
import com.example.kotlin.examen_moviles.framework.viewmodel.HistoricalViewModel

/**
 * Activity que muestra una lista de elementos históricos en un RecyclerView con capacidad de búsqueda
 * y filtros por categorías. Utiliza un ViewModel para gestionar los datos y un adaptador para mostrarlos.
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
     * inicializa el RecyclerView, activa el buscador, configura los filtros y observa los datos del ViewModel.
     *
     * @param savedInstanceState Estado de la actividad guardado anteriormente.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inicializarVinculo()
        inicializarRecyclerView()
        eschucharBuscador()
        inicializarListeners()
        inicializarFiltros()

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
     * Configura el RecyclerView con un adaptador y un GridLayoutManager de una columna.
     * Permite mostrar los elementos históricos en una lista.
     */
    private fun inicializarRecyclerView() {
        historicalAdapter = HistoricalAdapter()
        binding.recyclerViewHistorical.layoutManager = GridLayoutManager(this, 1)
        binding.recyclerViewHistorical.adapter = historicalAdapter
    }

    /**
     * Observa los datos del ViewModel. Cuando cambian, actualiza los datos del adaptador
     * y verifica si hay resultados para mostrar u ocultar el estado "Sin Resultados".
     * También inicializa los filtros una vez que los datos han sido cargados.
     */
    private fun observarViewModel() {
        viewModel.historicalLiveData.observe(this) { historical ->
            Log.d("HistoricalActivity", "Datos recibidos: $historical")
            historicalAdapter.establecerHistoricalData(historical, this)
            verificarEstadoResultados(historical.isEmpty())

            // Inicializa los filtros después de que los datos hayan sido cargados
            inicializarFiltros()
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
     * Escucha el campo de búsqueda y filtra los elementos históricos en tiempo real
     * a medida que el usuario escribe. Muestra u oculta el mensaje "Sin Resultados" si es necesario.
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
     * @param sinResultados Indica si no se encontraron elementos históricos en la lista.
     */
    private fun verificarEstadoResultados(sinResultados: Boolean) {
        binding.contenedorSinResultados.visibility = if (sinResultados) View.VISIBLE else View.GONE
        binding.recyclerViewHistorical.visibility = if (sinResultados) View.GONE else View.VISIBLE
    }

    /**
     * Inicializa los filtros de las categorías `category1` y `category2`, configurando los Spinners
     * correspondientes y sus eventos de selección.
     */
    private fun inicializarFiltros() {
        val categories1 = historicalAdapter.obtenerUnicos("category1")
        val categories2 = historicalAdapter.obtenerUnicos("category2")

        Log.d("HistoricalActivity", "Opciones para category1: $categories1")
        Log.d("HistoricalActivity", "Opciones para category2: $categories2")

        // Asegúrate de pasar listas válidas al configurar los Spinners
        configurarSpinner(binding.spinnerCategory1, categories1) { seleccion ->
            aplicarFiltros(seleccion, binding.spinnerCategory2.selectedItem.toString())
        }

        configurarSpinner(binding.spinnerCategory2, categories2) { seleccion ->
            aplicarFiltros(binding.spinnerCategory1.selectedItem.toString(), seleccion)
        }
    }


    /**
     * Aplica los filtros seleccionados por las categorías `category1` y `category2`.
     *
     * @param category1 Valor seleccionado para filtrar por la primera categoría.
     * @param category2 Valor seleccionado para filtrar por la segunda categoría.
     */
    private fun aplicarFiltros(category1: String, category2: String) {
        Log.d("HistoricalActivity", "Aplicando filtros: category1=$category1, category2=$category2")
        historicalAdapter.aplicarFiltro(category1, category2)
    }


    /**
     * Configura un Spinner con una lista de opciones y un listener para manejar la selección de elementos.
     *
     * @param spinner Spinner a configurar.
     * @param opciones Lista de opciones para el Spinner.
     * @param onItemSelected Callback que se ejecuta cuando un elemento es seleccionado.
     */
    private fun configurarSpinner(spinner: Spinner, opciones: List<String>, onItemSelected: (String) -> Unit) {
        Log.d("HistoricalActivity", "Configurando Spinner con opciones: $opciones")

        // Crea el adaptador con opciones filtradas
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val seleccion = parent?.getItemAtPosition(position) as String
                Log.d("HistoricalActivity", "Seleccionado en ${spinner.id}: $seleccion")
                onItemSelected(seleccion)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hacer nada
            }
        }
    }

}