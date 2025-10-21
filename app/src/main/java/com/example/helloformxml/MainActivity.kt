package com.example.helloformxml

import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.semantics.text
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.example.helloformxml.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Usar View Binding para acceder a las vistas de forma segura
    private lateinit var binding: ActivityMainBinding

    companion object {
        // Usar const val para constantes conocidas en tiempo de compilación
        private const val MAX_CARACTERES = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar y configurar la vista usando View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Limitar la longitud del EditText usando un InputFilter
        binding.etNombre.filters = arrayOf(InputFilter.LengthFilter(MAX_CARACTERES))

        // Configurar los listeners
        configurarTextWatcher()
        configurarBotonSaludar()
    }

    override fun onStart() {
        super.onStart()
        // Resetear la UI cuando la actividad se vuelve visible para el usuario
        resetUi()
    }

    /**
     * Configura el listener del EditText para reaccionar a los cambios de texto.
     * Usa doOnTextChanged de KTX para un código más limpio.
     */
    private fun configurarTextWatcher() {
        binding.etNombre.doOnTextChanged { text, _, _, _ ->
            // Actualizar el contador de caracteres
            val longitudActual = text?.length ?: 0
            binding.tvContador.text = getString(R.string.contador_formato, longitudActual, MAX_CARACTERES)

            // Habilitar el botón solo si el texto (sin espacios) no está vacío
            binding.btnSaludar.isEnabled = text.toString().trim().isNotEmpty()
        }
    }

    /**
     * Configura el OnClickListener del botón para saludar.
     */
    private fun configurarBotonSaludar() {
        binding.btnSaludar.setOnClickListener {
            val nombre = binding.etNombre.text.toString().trim()

            // Usar recursos de string y color para el mensaje
            binding.tvMensaje.text = getString(R.string.saludo, nombre)
            binding.tvMensaje.setTextColor(ContextCompat.getColor(this, R.color.texto_exito))

            // Ocultar el teclado
            ocultarTeclado()
        }
    }

    /**
     * Oculta el teclado virtual.
     */
    private fun ocultarTeclado() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etNombre.windowToken, 0)
    }

    /**
     * Resetea la interfaz a su estado inicial.
     */
    private fun resetUi() {
        binding.etNombre.text.clear()
        binding.tvMensaje.text = ""
        binding.btnSaludar.isEnabled = false
        // Usar un recurso de string para el estado inicial del contador
        binding.tvContador.text = getString(R.string.contador_inicial, MAX_CARACTERES)
    }
}
