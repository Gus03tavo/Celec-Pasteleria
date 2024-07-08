package com.temaApp.proyectofinal.adminSection.productsDB

import android.app.Dialog
import android.content.Intent
import android.os.Build
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
import androidx.activity.result.contract.ActivityResultContracts
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
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class ModifyProduct : AppCompatActivity() {

    private lateinit var btnVolver:ImageView
    private lateinit var dropcate:Spinner
    private lateinit var btnModificar:Button
    private lateinit var selected:String
    private lateinit var txtNombreModi:EditText
    private lateinit var txtPrecioModi:EditText
    private lateinit var txtStockModi:EditText
    private lateinit var txtIdModi:EditText
    private lateinit var imgProduct:ImageView
    private lateinit var btnBuscarProd:Button
    private lateinit var btnIngresoImgMoid:Button


    private lateinit var imgModify:ByteArray

    private lateinit var imgDB:ByteArray



    private  var imageBytes: ByteArray? =null
    val pickMedia=registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri->
        if(uri!=null){
            imgProduct.setImageURI(uri)

            val inputStream = contentResolver.openInputStream(uri)
            if (inputStream != null) {
                imageBytes = inputStream.readBytes()
            }
            inputStream?.close()

        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_modify_product)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initdropcate()
        initCompononents()
        modificarDatos()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initCompononents(){
        btnVolver=findViewById(R.id.btnVolver)
        btnModificar=findViewById(R.id.btnModificar)
        txtIdModi=findViewById(R.id.txtIdBuscar)
        btnBuscarProd=findViewById(R.id.btnBuscarProdu)
        txtNombreModi=findViewById(R.id.txtNombreModi)
        txtStockModi=findViewById(R.id.txtModiStock)
        txtPrecioModi=findViewById(R.id.txtPrecioModi)
        imgProduct=findViewById(R.id.imgProduModi)
        btnIngresoImgMoid=findViewById(R.id.btnIngresoImgModi)

        btnVolver.setOnClickListener {
            val intent=Intent(this,DashAdmin::class.java)
            startActivity(intent)
        }

        btnBuscarProd.setOnClickListener {
            busquedaDatos()
        }

        btnIngresoImgMoid.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

    }

    private fun modificarDatos(){

        btnModificar.setOnClickListener {
            capturarDatos()
        }

    }



    private fun showDialog() {
        val dialog= Dialog(this)
        dialog.setContentView(R.layout.msgmodificar)
        val btnContinuar: Button =dialog.findViewById(R.id.continuar)

        btnContinuar.setOnClickListener {
            val intent = Intent(this, ViewProducts::class.java)
            startActivity(intent)
        }
        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun busquedaDatos(){

        val id=txtIdModi.text.toString().toInt()


        CoroutineScope(Dispatchers.IO).launch {
            try {

                val rpta=RetrofitClientImg.webimg.buscaProduct(id)

                runOnUiThread {
                    if(rpta.isSuccessful){
                        val usuario=rpta.body()?.producto

                        if(usuario!=null){
                            txtNombreModi.setText(usuario.pro_nombre)
                            txtStockModi.setText(usuario.pro_stock.toString())
                            txtPrecioModi.setText(usuario.pro_precio.toString())
                            imgDB=usuario.pro_imagen
                            imgProduct.setImageIcon(usuario.pro_imagen.toIcon())
                            selectSavedCategory(usuario.pro_categoria)
                        }

                    }
                }

            }catch (e:Exception){
                Log.d("error",e.message.toString())
            }
        }



    }

    private fun initdropcate(){

        dropcate=findViewById(R.id.dropCate)

        val adapter=ArrayAdapter.createFromResource(this, R.array.spinner_items,
            android.R.layout.simple_spinner_dropdown_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dropcate.adapter=adapter

        dropcate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                // Handle item selection
                selected = parentView?.getItemAtPosition(position).toString()
                Toast.makeText(this@ModifyProduct, "Selected: $selected", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing
            }
        }

    }


    private fun capturarDatos() {
        val nombres = txtNombreModi.text.toString()
        val precios = txtPrecioModi.text.toString()
        val stocks = txtStockModi.text.toString()


        var validar = true

        if (nombres.isEmpty()) {
            validar = false
            txtNombreModi.setError("Ingresar nombre")
        }
        if (precios.isEmpty()) {
            validar = false
            txtPrecioModi.setError("Ingresar precio del producto")
        }
        if (stocks.isEmpty()) {
            validar = false
            txtStockModi.setError("Ingresar stock")
        }


        if (validar) {

            val nombrePart = nombres.toRequestBody("text/plain".toMediaTypeOrNull())
            val precioPart = precios.toRequestBody("text/plain".toMediaTypeOrNull())
            val categoriaPart = selected.toRequestBody("text/plain".toMediaTypeOrNull())
            val stockPart = stocks.toRequestBody("text/plain".toMediaTypeOrNull())
            val estadoPart = "1".toRequestBody("text/plain".toMediaTypeOrNull())



            Log.d("Nombre", imageBytes.toString())


            val byteArray: ByteArray = imageBytes ?: imgDB // tu ByteArray aquí
            val mimeType = "image/jpeg"
            val partName = "pro_imagen"
            val fileName = "image.jpg"
            val imagenPart = createMultipartBodyPartFromByteArray(byteArray, mimeType, partName, fileName)

            Log.d("Nombreeee", imagenPart.toString())

            CoroutineScope(Dispatchers.IO).launch {
                val response = imagenPart.let {
                    RetrofitClient.web.modifyProduct(
                        txtIdModi.text.toString().toInt(),
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
                                this@ModifyProduct,
                                "Respuesta nula",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        response.errorBody()?.let {
                            Log.d("Error", it.string())
                            Toast.makeText(
                                this@ModifyProduct,
                                "Error: ${it.string()}",
                                Toast.LENGTH_LONG
                            ).show()
                        } ?: run {
                            Log.d("Error", "Error desconocido")
                            Toast.makeText(
                                this@ModifyProduct,
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

    private fun selectSavedCategory(savedCategory: String) {
        val categories = resources.getStringArray(R.array.spinner_items)
        val position = categories.indexOf(savedCategory)
        if (position != -1) {
            dropcate.setSelection(position)
        }
    }
}