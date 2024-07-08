package com.temaApp.proyectofinal.store.categorias

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.temaApp.proyectofinal.R

class CategoriesAdapter(private val categories: List<ProductCategory>, private val onItemSelected:(Int) -> Unit):RecyclerView.Adapter<CategoriesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_product_category,parent,false)
        return CategoriesViewHolder(view)
    }

    override fun getItemCount(): Int =categories.size

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.render(categories[position],onItemSelected)
    }

}