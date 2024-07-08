package com.temaApp.proyectofinal.clienteSection.historialpedidos

data class HistorialProd(
    val pro_imagen: ByteArray,
    val pro_nombre: String,
    val pro_precio: Double,
    val deve_cant_vendida: Int,
    val total_venta: Double,
    val v_id: Int
)
