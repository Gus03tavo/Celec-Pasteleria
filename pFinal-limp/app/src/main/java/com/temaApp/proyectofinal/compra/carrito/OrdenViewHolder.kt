package com.temaApp.proyectofinal.compra.carrito

import android.graphics.Bitmap
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toIcon
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.temaApp.proyectofinal.R
import com.temaApp.proyectofinal.autenticacion.usuarioGlobal
import com.temaApp.proyectofinal.servicios.entidad.ProductoDtoGet
import com.temaApp.proyectofinal.servicios.entidad.ProductsCart

class OrdenViewHolder(
    itemView: View,
    private val buttonClickListener: OrdenAdapter.OrdenButtonClickListener
) : RecyclerView.ViewHolder(itemView) {

    private val productName: TextView = itemView.findViewById(R.id.nameOrden)
    private val productPrice: TextView = itemView.findViewById(R.id.priceOrden)
    private val productQuantity: TextView = itemView.findViewById(R.id.tvNumber)
    private val eliminarButton: Button = itemView.findViewById(R.id.btnEliminar)
    private val imagen:ImageView=itemView.findViewById(R.id.imgOrden)

    @RequiresApi(Build.VERSION_CODES.O)
    fun renderOrden(productsCart: ProductsCart) {
        productName.text = productsCart.pro_nombre
        productPrice.text = "Precio: S/. ${productsCart.pro_precio}"
        productQuantity.text = "${productsCart.cantidad}"
        imagen.setImageIcon(productsCart.pro_imagen.toIcon())

        // Configurar el clic del botón eliminar
        eliminarButton.setOnClickListener {
            buttonClickListener.onEliminarClick(adapterPosition, productsCart.pro_nombre, usuarioGlobal.usuario?.usu_id ?: 0)
        }
    }
}
