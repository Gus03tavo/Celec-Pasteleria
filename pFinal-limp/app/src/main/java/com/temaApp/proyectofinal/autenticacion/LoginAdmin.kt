package com.temaApp.proyectofinal.autenticacion

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
import com.temaApp.proyectofinal.adminSection.DashAdmin
import com.temaApp.proyectofinal.MainActivity
import com.temaApp.proyectofinal.R
import com.temaApp.proyectofinal.servicios.entidad.Usuario
import com.temaApp.proyectofinal.servicios.entidad.UsuarioDto
import com.temaApp.proyectofinal.servicios.servicio.RetrofitClient
import com.temaApp.proyectofinal.store.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object usuarioAdmin {
    var usuario: Usuario? = null
}


class LoginAdmin : AppCompatActivity() {
    private lateinit var btnlogins:Button
    private lateinit var btnVolver:ImageView

    private lateinit var textAdminUsurio: EditText
    private lateinit var textAdminPassw: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        asignar()
    }
    private fun asignar(){
        btnlogins=findViewById(R.id.btnLogins)
        btnVolver=findViewById(R.id.btnVolver)

        textAdminUsurio=findViewById(R.id.textAdminUsurio)
        textAdminPassw=findViewById(R.id.textAdminPassw)

        btnlogins.setOnClickListener {
            val intent=Intent(this, DashAdmin::class.java)
            startActivity(intent)
        }

        btnVolver.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        btnlogins.setOnClickListener {
            lifecycleScope.launch {
                val result=capturarData()

                if(result){
                    showDialog()
                }
                else{
                    mostarmensaje("Datos incorrectos")
                }
            }
        }

    }
    private suspend fun capturarData():Boolean{

        val email = textAdminUsurio.text.toString()
        val contra = textAdminPassw.text.toString()

        var validar = true

        if (email.isEmpty()) {
            validar = false
            textAdminUsurio.setError("Ingresar correo")
        }
        if (contra.isEmpty()) {
            validar = false
            textAdminPassw.setError("Ingresar contraseña")
        }

        var succesful=false
        if (validar) {


            var usuario= UsuarioDto(email,contra)

            val job = CoroutineScope(Dispatchers.IO).async {
                val response = RetrofitClient.web.loginAdmin(usuario)
                if (response.isSuccessful) {
                    response.body()?.let {
                        succesful = true
                        usuarioAdmin.usuario=it.usuario
                        Log.d("Response", it.message)
                        //Log.d("Response", it.usuario.usu_nombre)
                    } ?: run {
                        Log.d("Error", "Respuesta nula")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@LoginAdmin,
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
                                this@LoginAdmin,
                                "Error: ${it.string()}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } ?: run {
                        Log.d("Error", "Error desconocido")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@LoginAdmin,
                                "Error desconocido",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }

            job.await()
        }

        return succesful


    }




    fun showDialog() {
        val dialog= Dialog(this)
        dialog.setContentView(R.layout.msg_login)
        val btnContinuar:Button=dialog.findViewById(R.id.continuar)
        val nombre: TextView =dialog.findViewById(R.id.userNombre)
        nombre.text= usuarioAdmin.usuario?.usu_nombre

        btnContinuar.setOnClickListener {
            val intent = Intent(this, DashAdmin::class.java)
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