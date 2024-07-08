package com.temaApp.proyectofinal.servicios.entidad

data class ProductoDtoGet (
    var pro_id: Int,
    var pro_nombre: String,
    var pro_precio: Double,
    var pro_categoria: String,
    var pro_stock: Int,
    var pro_imagen: ByteArray,
    var estado: Char
)

