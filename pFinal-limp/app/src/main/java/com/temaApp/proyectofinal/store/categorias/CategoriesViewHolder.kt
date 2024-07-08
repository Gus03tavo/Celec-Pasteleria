package com.temaApp.proyectofinal.store.categorias

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.temaApp.proyectofinal.R

class CategoriesViewHolder(view:View):RecyclerView.ViewHolder(view) {

    private val tvCategoryName: TextView =view.findViewById(R.id.categoriaName)

    fun render(productCategory: ProductCategory, onItemSelected: (Int) -> Unit){

        itemView.setOnClickListener{
            onItemSelected(layoutPosition)
        }

        val color=if(productCategory.isSelected){
            R.drawable.bordercolorselected

        }else{
            R.drawable.bordercolor
        }

        tvCategoryName.setBackgroundResource(color)


        when(productCategory){


            ProductCategory.Cupcake -> {

                tvCategoryName.text="Cupcake"

            }
            ProductCategory.Otros -> {

                tvCategoryName.text="Otros"

            }
            ProductCategory.Panes -> {

                tvCategoryName.text="Panes"

            }
            ProductCategory.Postres -> {

                tvCategoryName.text="Postres"

            }


        }


    }
}