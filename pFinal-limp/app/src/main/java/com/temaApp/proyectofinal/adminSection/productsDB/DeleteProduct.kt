package com.temaApp.proyectofinal.adminSection.productsDB

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toIcon
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.temaApp.proyectofinal.adminSection.DashAdmin
import com.temaApp.proyectofinal.R
import com.temaApp.proyectofinal.adminSection.viewProducts.ViewProducts
import com.temaApp.proyectofinal.servicios.servicio.RetrofitClient
import com.temaApp.proyectofinal.servicios.servicio.RetrofitClientImg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class DeleteProduct : AppCompatActivity() {

    private lateinit var btnVolver:ImageView
    private lateinit var btnEliminar:Button

    private lateinit var txtNombreDelete:EditText
    private lateinit var imgDelete:ImageView
    private lateinit var txtIdDelete:EditText
    private lateinit var btnBuscarId:Button


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_delete_product)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initComponent()
        eliminarDatos()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun initComponent() {
        btnVolver = findViewById(R.id.btnVolver)
        btnEliminar = findViewById(R.id.btnEliminarPro)
        btnBuscarId=findViewById(R.id.btnBuscarIDdelete)
        txtIdDelete=findViewById(R.id.txtBuscarIDele)
        txtNombreDelete=findViewById(R.id.txtNombreDelete)
        imgDelete=findViewById(R.id.imgDelete)

        btnBuscarId.setOnClickListener {
            busquedaDatos()
        }


        btnVolver.setOnClickListener {
            val intent=Intent(this, DashAdmin::class.java)
            startActivity(intent)
        }
    }

    private fun eliminarDatos(){
        btnEliminar.setOnClickListener {
            capturarDatos()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun busquedaDatos(){
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val rpta= RetrofitClientImg.webimg.buscaProduct(txtIdDelete.text.toString().toInt())

                runOnUiThread {
                    if(rpta.isSuccessful){
                        val usuario=rpta.body()?.producto

                        if(usuario!=null){
                            txtNombreDelete.setText(usuario.pro_nombre)

                            imgDelete.setImageIcon(usuario.pro_imagen.toIcon())

                        }

                    }
                }

            }catch (e:Exception){
                Log.d("error",e.message.toString())
            }
        }
    }


    private fun capturarDatos() {
        val id=txtIdDelete.text.toString()


        var validar = true

        if (id.isEmpty()) {
            validar = false
            txtIdDelete.error = "Ingresar id"
        }



        if (validar) {

            CoroutineScope(Dispatchers.IO).launch {
                val response = id.let {
                    RetrofitClient.web.deleteProduct(
                        it.toInt()
                    )
                }
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            Log.d("Response", it.message)
                            showDialog()
                        } ?: run {
                            Log.d("Error", "Respuesta nula")
                            Toast.makeText(
                                this@DeleteProduct,
                                "Respuesta nula",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        response.errorBody()?.let {
                            Log.d("Error", it.string())
                            Toast.makeText(
                                this@DeleteProduct,
                                "Error: ${it.string()}",
                                Toast.LENGTH_LONG
                            ).show()
                        } ?: run {
                            Log.d("Error", "Error desconocido")
                            Toast.makeText(
                                this@DeleteProduct,
                                "Error desconocido",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }


    private fun showDialog() {
        val dialog= Dialog(this)
        dialog.setContentView(R.layout.msgdelete)
        val btnContinuar: Button =dialog.findViewById(R.id.continuar)

        btnContinuar.setOnClickListener {
            val intent = Intent(this, ViewProducts::class.java)
            startActivity(intent)
        }
        dialog.show()
    }
}