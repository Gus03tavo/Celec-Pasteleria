package com.temaApp.proyectofinal.compra.carrito

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toIcon
import androidx.recyclerview.widget.RecyclerView
import com.temaApp.proyectofinal.R
import com.temaApp.proyectofinal.autenticacion.usuarioGlobal
import com.temaApp.proyectofinal.servicios.entidad.ProductsCart

class OrdenAdapter(
    private var productos: MutableList<ProductsCart>,

    private val listener: OrdenButtonClickListener
) : RecyclerView.Adapter<OrdenViewHolder>() {

    interface OrdenButtonClickListener {
        fun onEliminarClick(position: Int, proNombre: String, usu_id: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdenViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_myorder, parent, false)
        return OrdenViewHolder(view, listener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: OrdenViewHolder, position: Int) {
        val producto = productos[position]

        holder.renderOrden(producto)
    }


    override fun getItemCount(): Int {
        return productos.size
    }


}
