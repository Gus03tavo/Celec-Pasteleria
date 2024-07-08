package com.temaApp.proyectofinal.clienteSection.historialpedidos

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toIcon
import androidx.recyclerview.widget.RecyclerView
import com.temaApp.proyectofinal.R
import com.temaApp.proyectofinal.servicios.entidad.ProductoDtoGet
import com.temaApp.proyectofinal.store.productoSelected.ProductoSelected

class historialpedidosViewHolder(view:View):RecyclerView.ViewHolder(view) {
    private val imgProducto: ImageView = itemView.findViewById(R.id.imgProdH)
    private val nombreProducto: TextView = itemView.findViewById(R.id.nameProdH)
    private val precioProducto: TextView = itemView.findViewById(R.id.precioProdH)
    private val cantidadProducto: TextView = itemView.findViewById(R.id.cantidadProdH)
    private val totalVenta: TextView = itemView.findViewById(R.id.totalProdH)
    private val idVenta: TextView = itemView.findViewById(R.id.ordenProdH)

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("DiscouragedApi", "SetTextI18n")
    fun bind(producto: HistorialProd) {
        imgProducto.setImageIcon(producto.pro_imagen.toIcon())
        nombreProducto.text = producto.pro_nombre
        precioProducto.text = "S/. ${producto.pro_precio}"
        cantidadProducto.text = "Cantidad: ${producto.deve_cant_vendida}"
        totalVenta.text = "Total S/. ${producto.total_venta}"
        idVenta.text = "ORD-${producto.v_id}" // Aquí puedes ajustar el formato del ID de venta según tus necesidades
    }
}