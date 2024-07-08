package com.temaApp.proyectofinal.store.productoSelected

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toIcon
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.temaApp.proyectofinal.R
import com.temaApp.proyectofinal.autenticacion.usuarioGlobal
import com.temaApp.proyectofinal.servicios.servicio.RetrofitClient
import com.temaApp.proyectofinal.store.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductoSelected : AppCompatActivity() {

    private lateinit var imgPostre: ImageView
    private lateinit var txtNombre: TextView
    private lateinit var txtPrecio: TextView
    private lateinit var txtCantidad: TextView
    private lateinit var btnDecrement: ImageButton
    private lateinit var btnIncrement: ImageButton
    private lateinit var btnAgregarCarrito: Button
    private lateinit var btnBack: ImageButton

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_producto_selected)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initComplements()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initComplements() {
        imgPostre = findViewById(R.id.imgPostre)
        txtNombre = findViewById(R.id.txtNombre)
        txtPrecio = findViewById(R.id.txtPrecio)
        txtCantidad = findViewById(R.id.txtCantidad)
        btnDecrement = findViewById(R.id.floatingActionButton3)
        btnIncrement = findViewById(R.id.floatingActionButton4)
        btnAgregarCarrito = findViewById(R.id.btnAgregarCarrito)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {
            val intent = Intent(this, Store::class.java)
            startActivity(intent)
        }

        val imagenResourceId = intent.getByteArrayExtra("imagen")
        val productName = intent.getStringExtra("name")
        val productPrice = intent.getDoubleExtra("precio", 0.0)
        val idProducto = intent.getIntExtra("idProducto", 0)

        if (imagenResourceId != null) {
            imgPostre.setImageIcon(imagenResourceId.toIcon())
        }
        txtNombre.text = productName.toString()
        txtPrecio.text = "S/." + productPrice.toString()

        btnIncrement.setOnClickListener {
            var cantidad = txtCantidad.text.toString().toInt()
            cantidad++
            txtCantidad.text = cantidad.toString()
            txtPrecio.text = "S/." + (productPrice * cantidad).toString()
        }

        btnDecrement.setOnClickListener {
            var cantidad = txtCantidad.text.toString().toInt()
            if (cantidad > 1) {
                cantidad--
                txtCantidad.text = cantidad.toString()
                txtPrecio.text = "S/." + (productPrice * cantidad).toString()
            }
        }

        btnAgregarCarrito.setOnClickListener {
            val userId = usuarioGlobal.usuario?.usu_id // Obtener el usu_id del usuario logueado

            if (idProducto != 0 && userId != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    val response = RetrofitClient.web.agregarProductoCarrito(
                        pro_id = idProducto,
                        usu_id = userId,
                        cantidad = txtCantidad.text.toString().toInt() // Asegúrate de enviar la cantidad correcta
                    )

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            Toast.makeText(imgPostre.context, "Producto agregado al carrito", Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(imgPostre.context, "Error al agregar producto", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Error: ID de producto o usuario no encontrado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
