package com.temaApp.proyectofinal.miInbox

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.temaApp.proyectofinal.R


class miinboxViewHolder(view:View):RecyclerView.ViewHolder(view) {
    private val boxComentario:TextView = view.findViewById(R.id.boxComentario)
    private val boxFecha: TextView = view.findViewById(R.id.boxFecha)

    fun message(miInboxZcaja: MiInboxZcaja){
        boxComentario.text = miInboxZcaja.comentario.toString()
        boxFecha.text = miInboxZcaja.fecha.toString()

    }
}


