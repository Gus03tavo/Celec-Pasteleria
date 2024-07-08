package com.temaApp.proyectofinal.miInbox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.temaApp.proyectofinal.R


class miinboxAdapter(private val alertmansaje : List<MiInboxZcaja>):RecyclerView.Adapter<miinboxViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): miinboxViewHolder {
        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.item_myinbox_msg,parent,false)
        return miinboxViewHolder(view)

    }
    override fun getItemCount(): Int = alertmansaje.size



    override fun onBindViewHolder(holder: miinboxViewHolder, position: Int) {
        holder.message(alertmansaje[position])
    }

}