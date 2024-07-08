package com.temaApp.proyectofinal.servicios.servicio

import com.google.gson.annotations.SerializedName
import com.temaApp.proyectofinal.servicios.entidad.Usuario

data class CreateClienteResponse (
    val message: String,
    val id: Int? = null
)

data class ClienteResponse(
    @SerializedName("clientes") var listarClientes:ArrayList<Usuario>
)