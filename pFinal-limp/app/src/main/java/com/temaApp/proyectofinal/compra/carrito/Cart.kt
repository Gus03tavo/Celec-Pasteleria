package com.temaApp.proyectofinal.compra.carrito

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.temaApp.proyectofinal.R
import com.temaApp.proyectofinal.autenticacion.usuarioGlobal
import com.temaApp.proyectofinal.compra.CheckOut
import com.temaApp.proyectofinal.servicios.entidad.ProductsCart
import com.temaApp.proyectofinal.servicios.servicio.RetrofitClient
import com.temaApp.proyectofinal.servicios.servicio.RetrofitClientImg
import com.temaApp.proyectofinal.servicios.servicio.RetrofitClientImgg
import com.temaApp.proyectofinal.store.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.IOException

class Cart : AppCompatActivity(), OrdenAdapter.OrdenButtonClickListener {

    private lateinit var rvProductosOrden: RecyclerView
    private lateinit var ordenAdapter: OrdenAdapter


    private lateinit var btnBack: ImageView
    private lateinit var checkoutBtn: Button
    private lateinit var totalTextView: TextView

    private lateinit var listaProductosCart:MutableList<ProductsCart>


    companion object {
        const val IGV = 0.18
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.compruebapedido)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.comprueba)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initComponents()
        initUi()
        obtenerDatosCarrito()

    }

    private fun initComponents() {
        rvProductosOrden = findViewById(R.id.productosOrden)
        btnBack = findViewById(R.id.back)
        checkoutBtn = findViewById(R.id.btncheckout)
        totalTextView = findViewById(R.id.totalcom)


        btnBack.setOnClickListener {
            startActivity(Intent(this, Store::class.java))
        }

        checkoutBtn.setOnClickListener {
            startActivity(Intent(this, CheckOut::class.java))
        }
    }



    private fun obtenerDatosCarrito() {
        val userId = usuarioGlobal.usuario?.usu_id ?: 0

        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClientImg.webimg.getProductsInCart(userId)
            runOnUiThread {
                if(rpta.isSuccessful){
                    listaProductosCart = rpta.body()!!.listarProductos
                    ordenAdapter = OrdenAdapter(listaProductosCart, this@Cart)
                    mostrarDatos()
                    updateTotal()
                }
            }
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun mostrarDatos() {
        rvProductosOrden.adapter = ordenAdapter
        ordenAdapter.notifyDataSetChanged()
    }

    private fun initUi() {
        rvProductosOrden.layoutManager = GridLayoutManager(this, 1)

    }

    override fun onEliminarClick(position: Int, proNombre: String, userId: Int) {
        Log.d("Cart", "Eliminando producto $proNombre del carrito del usuario $userId")
        GlobalScope.launch(Dispatchers.Main) {
            var responseBody: ResponseBody? = null
            try {
                // Llamar a la función pasando proNombre como String
                val response = RetrofitClient.web.eliminarProductoDelCarrito(userId, proNombre)
                if (response.isSuccessful) {
                    listaProductosCart.removeAt(position)

                    ordenAdapter.notifyItemRemoved(position)
                    updateTotal()
                } else {
                    Log.e("Cart", "Error en la respuesta: ${response.code()}")
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("Cart", "Excepción en la conexión: ${e.message}")
            } finally {
                responseBody?.close()
            }
        }
        updateTotal()
    }

    private fun updateTotal() {
        val subtotal = listaProductosCart.sumOf { producto ->
            val cantidad = listaProductosCart.find { orden -> orden.pro_id == producto.pro_id }?.cantidad ?: 0
            producto.pro_precio * cantidad
        }
        val total = subtotal
        totalTextView.text = String.format("S/. %.2f", total)
    }

}
