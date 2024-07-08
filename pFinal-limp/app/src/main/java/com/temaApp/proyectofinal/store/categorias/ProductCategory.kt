package com.temaApp.proyectofinal.store.categorias

sealed class ProductCategory(var isSelected:Boolean=false) {


    object Cupcake: ProductCategory(true)
    object Postres: ProductCategory()
    object Panes: ProductCategory()
    object Otros: ProductCategory()

    fun getCategoryName(): String {
        return when (this) {

            Cupcake -> "Cupcakes"
            Postres -> "Postres"
            Panes -> "Panes"
            Otros -> "Otros"
        }
    }
}