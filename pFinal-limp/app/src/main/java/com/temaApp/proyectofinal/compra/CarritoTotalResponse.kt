package com.temaApp.proyectofinal.compra

data class CarritoTotalResponse(
    val subtotal: Double,
    val igv: Double,
    val total: Double
)