package com.temaApp.proyectofinal.clienteSection.historialpedidos

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.temaApp.proyectofinal.R
import com.temaApp.proyectofinal.autenticacion.usuarioGlobal
import com.temaApp.proyectofinal.clienteSection.miInbox.MiInbox
import com.temaApp.proyectofinal.clienteSection.profileDetails.Profile
import com.temaApp.proyectofinal.store.Store
import com.temaApp.proyectofinal.compra.carrito.Cart
import com.temaApp.proyectofinal.servicios.entidad.ProductoDtoGet
import com.temaApp.proyectofinal.servicios.servicio.RetrofitClientImg
import com.temaApp.proyectofinal.store.productos.ProductAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.http.Tag


class Historial : AppCompatActivity() {
    private lateinit var rvProducts: RecyclerView
    private lateinit var historialAdapter: historialpedidosAdapter

    private lateinit var store: ImageView
    private lateinit var cart: ImageView
    private lateinit var message: ImageView
    private lateinit var profile: ImageView

    private var listaProductos: MutableList<HistorialProd> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historialpedidos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initComponents()
        initUi()
        cargarDatos()
    }

    private fun initComponents(){
        rvProducts=findViewById(R.id.historialPro)
        store=findViewById(R.id.store)
        cart=findViewById(R.id.cart)
        message=findViewById(R.id.message)
        profile=findViewById(R.id.profile)

        store.setOnClickListener{
            startActivity(Intent(this, Store::class.java))
        }
        cart.setOnClickListener{
            startActivity(Intent(this, Cart::class.java))
        }
        message.setOnClickListener{
            startActivity(Intent(this, MiInbox::class.java))
        }
        profile.setOnClickListener{
            startActivity(Intent(this, Profile::class.java))
        }
    }

    private fun initUi(){
        historialAdapter = historialpedidosAdapter(listaProductos)
        rvProducts.layoutManager = GridLayoutManager(this, 1)
        rvProducts.adapter = historialAdapter
    }

    private fun cargarDatos(){
        val userId = usuarioGlobal.usuario?.usu_id ?: 0
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClientImg.webimg.HistorialProducts(userId)
                if (response.isSuccessful) {
                    val historialResponse = response.body()
                    historialResponse?.let {
                        listaProductos.clear()
                        listaProductos.addAll(it.historialProductos)
                        runOnUiThread {
                            historialAdapter.notifyDataSetChanged()
                        }
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("IF", "Unsuccessful response: ${response.code()} - $errorBody")
                }
            } catch (e: Exception) {
                Log.e("CATCH", "Exception: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}