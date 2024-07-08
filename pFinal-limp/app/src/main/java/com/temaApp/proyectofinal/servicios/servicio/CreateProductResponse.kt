package com.temaApp.proyectofinal.servicios.servicio

import com.google.gson.annotations.SerializedName
import com.temaApp.proyectofinal.clienteSection.historialpedidos.HistorialProd
import com.temaApp.proyectofinal.servicios.entidad.ProductoDtoGet
import com.temaApp.proyectofinal.servicios.entidad.ProductsCart
import com.temaApp.proyectofinal.servicios.entidad.Usuario

data class CreateProductResponse(
    val message: String,
    val id: Int? = null
)

data class LoginClienteResponse(
    val message: String,
    @SerializedName("usuario") var usuario: Usuario
)

data class ProductoResponse(
    @SerializedName("productos") var listarProductos:ArrayList<ProductoDtoGet>
)

data class ProductoCartResponse(
    @SerializedName("productos") var listarProductos:ArrayList<ProductsCart>
)

data class ProductoBusquedaResponse(
    @SerializedName("producto") var producto:ProductoDtoGet
)

data class HistorialResponse(
    @SerializedName("historialprod") var historialProductos:MutableList<HistorialProd>
)