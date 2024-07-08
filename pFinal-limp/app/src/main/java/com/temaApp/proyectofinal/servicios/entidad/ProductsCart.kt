package com.temaApp.proyectofinal.servicios.entidad

data class ProductsCart (
    var pro_id: Int,
    var pro_nombre: String,
    var pro_precio: Double,
    var pro_imagen: ByteArray,
    var cantidad: Int
)