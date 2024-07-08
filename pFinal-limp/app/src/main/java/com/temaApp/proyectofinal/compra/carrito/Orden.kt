package com.temaApp.proyectofinal.compra.carrito

data class Orden(
    val pro_id: Int,
    val pro_nombre: String,
    val pro_precio: Double,
    val pro_imagen: ByteArray, // Campo para almacenar los bytes de la imagen
    var cantidad: Int
)
