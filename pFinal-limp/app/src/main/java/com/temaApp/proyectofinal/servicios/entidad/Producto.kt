package com.temaApp.proyectofinal.servicios.entidad

data class Producto (

    var pro_nombre: String,
    var pro_precio: Double,
    var pro_categoria: String,
    var pro_stock: Int,
    var pro_imagen: ByteArray,
    var estado: Char
)

