package com.temaApp.proyectofinal.servicios.entidad

data class Usuario (

    val usu_id: Int,
    var usu_nombre: String,
    var usu_apellidos: String,
    var usu_email: String,
    var usu_ncelular: String,
    var usu_password: String,
    val estado: String

    )