package com.temaApp.proyectofinal.adminSection.viewProducts

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.temaApp.proyectofinal.adminSection.DashAdmin
import com.temaApp.proyectofinal.R
import com.temaApp.proyectofinal.modeloVistaControlador.modelo.ProductoDAO
import com.temaApp.proyectofinal.servicios.entidad.ProductoDtoGet
import com.temaApp.proyectofinal.servicios.servicio.RetrofitClientImg
import com.temaApp.proyectofinal.store.productos.ProductAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ViewProducts : AppCompatActivity() {
    private lateinit var rvViewProducts: RecyclerView
    private lateinit var viewProductsAdapter: ViewProductsAdapter
    private lateinit var menuBack:Button
    private lateinit var btnVolver:ImageView

    private var listaProductos:ArrayList<ProductoDtoGet> = ArrayList()
    private var productoDAO=ProductoDAO(this)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_products)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initComponents()
        cargarDatos()
        initUi()

    }


    private fun initComponents(){
        rvViewProducts=findViewById(R.id.rvViewPorducts)
        btnVolver=findViewById(R.id.btnVolver)
        menuBack=findViewById(R.id.btnMenuBack)

        btnVolver.setOnClickListener {
            val intent=Intent(this,DashAdmin::class.java)
            startActivity(intent)
        }
        menuBack.setOnClickListener {
            val intent=Intent(this,DashAdmin::class.java)
            startActivity(intent)
        }
    }
    private fun cargarDatos(){

        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClientImg.webimg.getProducts()
            runOnUiThread {
                if(rpta.isSuccessful){
                    listaProductos = rpta.body()!!.listarProductos
                    viewProductsAdapter= ViewProductsAdapter(listaProductos)
                    mostrarDatos()
                }
            }
        }
    }

    private fun mostrarDatos() {
        rvViewProducts.adapter=viewProductsAdapter
    }

    private fun initUi(){

        rvViewProducts.layoutManager= LinearLayoutManager(
            this
        )

    }
}