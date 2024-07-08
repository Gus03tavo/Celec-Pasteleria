package com.temaApp.proyectofinal.adminSection.productsDB

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.temaApp.proyectofinal.adminSection.DashAdmin
import com.temaApp.proyectofinal.R
import com.temaApp.proyectofinal.servicios.servicio.RetrofitClient
import com.temaApp.proyectofinal.adminSection.viewProducts.ViewProducts
import com.temaApp.proyectofinal.autenticacion.usuarioGlobal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody


class CrearProductoActivity : AppCompatActivity() {
    private lateinit var btnvolver:ImageView
    private lateinit var spiner: Spinner
    private lateinit var selected:String

    private lateinit var nombre:EditText
    private lateinit var precio:EditText

    private lateinit var stock:EditText
    private lateinit var imagen:ImageView
    private lateinit var btnguardar: Button
    private lateinit var btnSelect: Button



    private lateinit var imageBytes:ByteArray
    val pickMedia=registerForActivityResult(PickVisualMedia()){ uri->
        if(uri!=null){
            imagen.setImageURI(uri)

            val inputStream = contentResolver.openInputStream(uri)
            if (inputStream != null) {
                imageBytes = inputStream.readBytes()
            }
            inputStream?.close()

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_producto2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        asignar()
        listar()
    }

    private fun asignar(){
        btnvolver=findViewById(R.id.btnVolver)
        nombre=findViewById(R.id.txtCnombre)
        precio=findViewById(R.id.txtCprecio)
        stock=findViewById(R.id.txtCstock)
        imagen=findViewById(R.id.imageView)


        btnguardar=findViewById(R.id.btnCregistrar)
        btnSelect=findViewById(R.id.btnCingresarimagen)

        btnSelect.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))

        }
        btnguardar.setOnClickListener {
            capturarDatos()
        }



        btnvolver.setOnClickListener {
            val intent = Intent(this, DashAdmin::class.java)
            startActivity(intent)
        }
    }

    private fun capturarDatos() {
        val nombres = nombre.text.toString()
        val precios = precio.text.toString()
        val stocks = stock.text.toString()


        var validar = true

        if (nombres.isEmpty()) {
            validar = false
            nombre.setError("Ingresar nombre")
        }
        if (precios.isEmpty()) {
            validar = false
            precio.setError("Ingresar precio del producto")
        }
        if (stocks.isEmpty()) {
            validar = false
            stock.setError("Ingresar stock")
        }


        if (validar) {

            val nombrePart = nombres.toRequestBody("text/plain".toMediaTypeOrNull())
            val precioPart = precios.toRequestBody("text/plain".toMediaTypeOrNull())
            val categoriaPart = selected.toRequestBody("text/plain".toMediaTypeOrNull())
            val stockPart = stocks.toRequestBody("text/plain".toMediaTypeOrNull())
            val estadoPart = "1".toRequestBody("text/plain".toMediaTypeOrNull())



            Log.d("Nombre", imageBytes.toString())


            val byteArray: ByteArray = imageBytes// tu ByteArray aquí
            val mimeType = "image/jpeg"
            val partName = "pro_imagen"
            val fileName = "image.jpg"
            val imagenPart = createMultipartBodyPartFromByteArray(byteArray, mimeType, partName, fileName)

            Log.d("Nombreeee", imagenPart.toString())

            CoroutineScope(Dispatchers.IO).launch {
                val response = imagenPart.let {
                    RetrofitClient.web.createProduct(
                        nombrePart,
                        precioPart,
                        categoriaPart,
                        stockPart,
                        it,
                        estadoPart
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
                                this@CrearProductoActivity,
                                "Respuesta nula",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        response.errorBody()?.let {
                            Log.d("Error", it.string())
                            Toast.makeText(
                                this@CrearProductoActivity,
                                "Error: ${it.string()}",
                                Toast.LENGTH_LONG
                            ).show()
                        } ?: run {
                            Log.d("Error", "Error desconocido")
                            Toast.makeText(
                                this@CrearProductoActivity,
                                "Error desconocido",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }


    fun createMultipartBodyPartFromByteArray(byteArray: ByteArray, mimeType: String, partName: String, fileName: String): MultipartBody.Part {
        // Convertir ByteArray a RequestBody
        val requestBody = byteArray.toRequestBody(mimeType.toMediaTypeOrNull(), 0, byteArray.size)
        // Crear MultipartBody.Part a partir de RequestBody
        return MultipartBody.Part.createFormData(partName, fileName, requestBody)
    }




    fun showDialog() {
        val dialog= Dialog(this)
        dialog.setContentView(R.layout.msgcrear)
        val btnContinuar:Button=dialog.findViewById(R.id.continuar)

        btnContinuar.setOnClickListener {
            val intent = Intent(this, ViewProducts::class.java)
            startActivity(intent)
        }
        dialog.show()
    }
     fun mostarmensaje(mensaje: String) {
        val ventana= AlertDialog.Builder(this)
        ventana.setTitle("Mensaje Informativo")
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar", DialogInterface.OnClickListener{ dialog, which ->

            val intent = Intent(this, ViewProducts::class.java)
            startActivity(intent)
        })
        ventana.create()
        ventana.show()
    }

    fun listar(){
        spiner = findViewById(R.id.spListar)
        val adapter=ArrayAdapter.createFromResource(this, R.array.spinner_items,
            android.R.layout.simple_spinner_dropdown_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spiner.adapter=adapter

        spiner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                // Handle item selection
                selected = parentView?.getItemAtPosition(position).toString()
                Toast.makeText(this@CrearProductoActivity, "Selected: $selected", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }
}