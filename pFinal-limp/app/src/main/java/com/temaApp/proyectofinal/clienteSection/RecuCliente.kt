package com.temaApp.proyectofinal.clienteSection

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.temaApp.proyectofinal.R
import com.temaApp.proyectofinal.autenticacion.LoginCliente
import com.temaApp.proyectofinal.servicios.entidad.UsuarioDTOChangeContra
import com.temaApp.proyectofinal.servicios.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecuCliente : AppCompatActivity() {
    private lateinit var btnBack : ImageView

    private lateinit var txtCorreo:EditText
    private lateinit var txtContra:EditText
    private lateinit var txtValidContra:EditText

    private lateinit var btnChangeContra:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recucliente)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initComponents()
    }

    fun initComponents(){
        btnBack = findViewById(R.id.btnBack)
        txtCorreo=findViewById(R.id.txtCorreo)
        txtContra=findViewById(R.id.txtContra)
        txtValidContra=findViewById(R.id.txtValidContra)
        btnChangeContra=findViewById(R.id.btnChangeContra)


        btnBack.setOnClickListener{
            val intent = Intent(this, LoginCliente::class.java)
            startActivity(intent)
        }

        btnChangeContra.setOnClickListener {
            capturarDatos()
        }


    }

    private fun capturarDatos(){
        val correo=txtCorreo.text.toString()
        val contra=txtContra.text.toString()
        val validContra=txtValidContra.toString()


        var validar = true


        if (correo.isEmpty()) {
            validar = false
            txtCorreo.error = "Ingresar correo"
        }


        if (contra.isEmpty()) {
            validar = false
            txtContra.error = "Ingresar contraseña nueva"
        }


        if (validContra.isEmpty()) {
            validar = false
            txtValidContra.error = "Validad contraseña nueva"
        }



        if (validar) {

            val usuario=UsuarioDTOChangeContra(contra,correo)
            Log.d("ded",contra)
            CoroutineScope(Dispatchers.IO).launch {
                val response =
                    RetrofitClient.web.changePassword(
                        usuario
                    )

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            Log.d("Response", it.message)
                            mostarmensaje(it.message)
                        } ?: run {
                            Log.d("Error", "Respuesta nula")
                            Toast.makeText(
                                this@RecuCliente,
                                "Respuesta nula",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        response.errorBody()?.let {
                            Log.d("Error", it.string())
                            Toast.makeText(
                                this@RecuCliente,
                                "Error: ${it.string()}",
                                Toast.LENGTH_LONG
                            ).show()
                        } ?: run {
                            Log.d("Error", "Error desconocido")
                            Toast.makeText(
                                this@RecuCliente,
                                "Error desconocido",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }



    }


    fun mostarmensaje(mensaje: String) {
        val ventana= AlertDialog.Builder(this)
        ventana.setTitle("Mensaje Informativo")
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->

            val intent=Intent(this,LoginCliente::class.java)
            startActivity(intent)
        })
        ventana.create()
        ventana.show()
    }




}