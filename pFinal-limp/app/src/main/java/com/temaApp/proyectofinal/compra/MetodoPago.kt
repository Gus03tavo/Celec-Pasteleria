package com.temaApp.proyectofinal.compra


import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.temaApp.proyectofinal.R
import android.widget.Spinner

class MetodoPago : AppCompatActivity() {

    private lateinit var   btnRegistra_metodo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.metodopago)
        val spinnerTipoTarjeta: Spinner = findViewById(R.id.spinnerTipoTarjeta)

        // Definir las opciones para el Spinner
        val opciones = arrayOf("Visa", "Mastercard", "American Express", "Otros")

        // Crear un ArrayAdapter utilizando las opciones y el layout predefinido para el Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)

        // Especificar el layout que se usará cuando la lista de opciones aparezca
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Aplicar el adaptador al Spinner
        spinnerTipoTarjeta.adapter = adapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.metodop)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        iniciaCheckOut()
    }


    private fun iniciaCheckOut() {
        btnRegistra_metodo = findViewById(R.id.btnRegistra_metodo)
        btnRegistra_metodo.setOnClickListener {
            val intent = Intent(this, CheckOut::class.java)
            startActivity(intent)
        }
    }
}