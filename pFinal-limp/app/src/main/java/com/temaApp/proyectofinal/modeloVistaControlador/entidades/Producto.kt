package com.temaApp.proyectofinal.modeloVistaControlador.entidades

class Producto {

    var id:Int = 0
    lateinit var nombre:String
    lateinit var categoria:String
    var precio:Double = 0.0
    var stock:Int=0
    lateinit var picture:ByteArray

}