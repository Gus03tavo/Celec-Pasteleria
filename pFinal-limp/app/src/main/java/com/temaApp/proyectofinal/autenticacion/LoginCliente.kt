package com.temaApp.proyectofinal.autenticacion

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.temaApp.proyectofinal.MainActivity
import com.temaApp.proyectofinal.R
import com.temaApp.proyectofinal.adminSection.viewProducts.ViewProducts
import com.temaApp.proyectofinal.store.Store
import com.temaApp.proyectofinal.clienteSection.CrearCliente
import com.temaApp.proyectofinal.clienteSection.RecuCliente
import com.temaApp.proyectofinal.servicios.entidad.Usuario
import com.temaApp.proyectofinal.servicios.entidad.UsuarioDto
import com.temaApp.proyectofinal.servicios.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

object usuarioGlobal {
    var usuario: Usuario? = null
    var usuariid: Int? = null
}

class LoginCliente : AppCompatActivity() {
    private lateinit var checkBox: CheckBox
    private lateinit var tvRegistrarse:TextView
    private lateinit var btnBack:ImageView
    private lateinit var tvOlvida:TextView

    private lateinit var txtEmailCliente:EditText
    private lateinit var txtContraCliente:EditText

    private lateinit var btnIngresar:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_logincliente)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initComponents()
    }

    private fun initComponents(){



        txtEmailCliente = findViewById(R.id.txtEmailCliente)
        txtContraCliente = findViewById(R.id.txtContraCli)
        tvRegistrarse = findViewById(R.id.tvRegistrarse)
        val subrayado = SpannableString(tvRegistrarse.text)
        subrayado.setSpan(UnderlineSpan(),0,tvRegistrarse.text.length,0)
        tvRegistrarse.text = subrayado
        tvRegistrarse.setOnClickListener {


            val intent = Intent(this, CrearCliente::class.java)
            startActivity(intent)

        }

        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        tvOlvida = findViewById(R.id.tvOlvidoContra)
        tvOlvida.setOnClickListener {
            val intent = Intent(this, RecuCliente::class.java)
            startActivity(intent)
        }

        btnIngresar=findViewById(R.id.btnIngresar)

        btnIngresar.setOnClickListener {

            lifecycleScope.launch {
                val result=capturarDatos()

                if(result){
                    showDialog()
                }
                else{
                    mostarmensaje("Datos incorrectos")
                }
            }

        }


    }

    private suspend fun capturarDatos():Boolean{

        val email = txtEmailCliente.text.toString()
        val contra = txtContraCliente.text.toString()

        var validar = true

        if (email.isEmpty()) {
            validar = false
            txtEmailCliente.setError("Ingresar correo")
        }
        if (contra.isEmpty()) {
            validar = false
            txtContraCliente.setError("Ingresar contraseña")
        }

        var succesful=false
        if (validar) {


            var usuario=UsuarioDto(email,contra)

            val job = CoroutineScope(Dispatchers.IO).async {
                val response = RetrofitClient.web.loginCliente(usuario)
                if (response.isSuccessful) {
                    response.body()?.let {
                        succesful = true
                        usuarioGlobal.usuario = it.usuario
                        usuarioGlobal.usuariid = it.usuario.usu_id
                        Log.d("Response", it.message)
                        Log.d("Response", it.usuario.usu_nombre)
                    } ?: run {
                        Log.d("Error", "Respuesta nula")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@LoginCliente,
                                "Respuesta nula",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } else {
                    response.errorBody()?.let {
                        Log.d("Error", it.string())
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@LoginCliente,
                                "Error: ${it.string()}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } ?: run {
                        Log.d("Error", "Error desconocido")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@LoginCliente,
                                "Error desconocido",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }

            job.await()
        }

        //Log.d("Bool", succesful.toString())
        //Log.d("Bool", usuarioGlobal.usuario?.usu_nombre.toString())

        return succesful


    }


    fun showDialog() {
        val dialog= Dialog(this)
        dialog.setContentView(R.layout.msg_login)
        val btnContinuar:Button=dialog.findViewById(R.id.continuar)
        val nombre:TextView=dialog.findViewById(R.id.userNombre)
        nombre.text= usuarioGlobal.usuario?.usu_nombre

        btnContinuar.setOnClickListener {
            val intent = Intent(this, Store::class.java)
            startActivity(intent)
        }
        dialog.show()
    }

    fun mostarmensaje(mensaje: String) {
        val ventana= AlertDialog.Builder(this)
        ventana.setTitle("Mensaje Informativo")
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar", null)
        ventana.create()
        ventana.show()
    }
}