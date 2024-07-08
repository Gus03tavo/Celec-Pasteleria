package com.temaApp.proyectofinal.store.productos

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.temaApp.proyectofinal.R
import com.temaApp.proyectofinal.servicios.entidad.ProductoDtoGet

class ProductAdapter (var productos:MutableList<ProductoDtoGet>):RecyclerView.Adapter<ProductViewHolder>(){


     var filteredProductos: List<ProductoDtoGet> = productos


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_product_layout,parent,false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int=filteredProductos.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.render(filteredProductos[position])
    }


}