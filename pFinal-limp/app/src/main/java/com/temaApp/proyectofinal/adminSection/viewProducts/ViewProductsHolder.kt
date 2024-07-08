package com.temaApp.proyectofinal.adminSection.viewProducts

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toIcon
import androidx.recyclerview.widget.RecyclerView
import com.temaApp.proyectofinal.R
import com.temaApp.proyectofinal.modeloVistaControlador.entidades.Producto
import com.temaApp.proyectofinal.servicios.entidad.ProductoDtoGet

class ViewProductsHolder(view: View): RecyclerView.ViewHolder(view) {

    private val imgProduct: ImageView =view.findViewById(R.id.imgPostre)
    private val txtNamePro: TextView =view.findViewById(R.id.textNombre)
    private val txtstock: TextView =view.findViewById(R.id.textStock)
    private val txtID: TextView =view.findViewById(R.id.textID)
    private val txtPrecio: TextView =view.findViewById(R.id.txtPrecio)


    @RequiresApi(Build.VERSION_CODES.O)
    fun render(producto: ProductoDtoGet){

        txtNamePro.text=producto.pro_nombre
        txtstock.text=producto.pro_stock.toString()
        txtID.text=producto.pro_id.toString()
        txtPrecio.text=producto.pro_precio.toString()
        imgProduct.setImageIcon(producto.pro_imagen.toIcon())




    }
}