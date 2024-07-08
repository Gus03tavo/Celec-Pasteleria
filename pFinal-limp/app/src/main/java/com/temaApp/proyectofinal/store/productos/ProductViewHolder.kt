package com.temaApp.proyectofinal.store.productos

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toIcon
import androidx.recyclerview.widget.RecyclerView
import com.temaApp.proyectofinal.R
import com.temaApp.proyectofinal.autenticacion.usuarioGlobal
import com.temaApp.proyectofinal.store.productoSelected.ProductoSelected
import com.temaApp.proyectofinal.servicios.entidad.ProductoDtoGet

class ProductViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val imgProduct: ImageView = view.findViewById(R.id.imgProduct)
    private val txtNamePro: TextView = view.findViewById(R.id.nameProduct)
    private val txtPrice: TextView = view.findViewById(R.id.priceProduct)

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("DiscouragedApi", "SetTextI18n")
    fun render(product: ProductoDtoGet) {
        imgProduct.setImageIcon(product.pro_imagen.toIcon())
        txtNamePro.text = product.pro_nombre
        txtPrice.text = "S/." + product.pro_precio.toString()

        itemView.setOnClickListener {
            val intent = Intent(imgProduct.context, ProductoSelected::class.java)
            intent.putExtra("imagen", product.pro_imagen)
            intent.putExtra("name", product.pro_nombre)
            intent.putExtra("precio", product.pro_precio)
            intent.putExtra("idProducto", product.pro_id) // Add this line to pass the product ID
            imgProduct.context.startActivity(intent)
        }
    }
}
