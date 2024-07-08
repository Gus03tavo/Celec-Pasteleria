package com.temaApp.proyectofinal.clienteSection.historialpedidos

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.temaApp.proyectofinal.R
import com.temaApp.proyectofinal.servicios.entidad.ProductoDtoGet

class historialpedidosAdapter(var productos: MutableList<HistorialProd>):RecyclerView.Adapter<historialpedidosViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): historialpedidosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_historial_layout, parent, false)
        return historialpedidosViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: historialpedidosViewHolder, position: Int) {
        holder.bind(productos[position])
    }

    override fun getItemCount(): Int {
        return productos.size
    }
}