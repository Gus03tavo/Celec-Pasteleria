package com.temaApp.proyectofinal.adminSection.viewProducts

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.temaApp.proyectofinal.R
import com.temaApp.proyectofinal.modeloVistaControlador.entidades.Producto
import com.temaApp.proyectofinal.servicios.entidad.ProductoDtoGet

class ViewProductsAdapter (private val productos:List<ProductoDtoGet>):
    RecyclerView.Adapter<ViewProductsHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewProductsHolder {
        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.item_product_viewproducts,parent,false)
        return ViewProductsHolder(view)
    }

    override fun getItemCount(): Int =productos.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewProductsHolder, position: Int) {
        holder.render(productos[position])
    }


}