package com.temaApp.proyectofinal.modeloVistaControlador.modelo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.temaApp.proyectofinal.modeloVistaControlador.entidades.Producto
import com.temaApp.proyectofinal.modeloVistaControlador.util.BaseDatos

class ProductoDAO(context:Context) {

    private var base:BaseDatos= BaseDatos(context)

    fun registrarProductos(producto:Producto):String{

        var respuesta:String=""
        var db=base.writableDatabase

        try {
            val valores=ContentValues()
            valores.put("nombre",producto.nombre)
           valores.put("categoria",producto.categoria)
            valores.put("precio",producto.precio)
            valores.put("stock",producto.stock)
            valores.put("picture",producto.picture)

            var r=db.insert("productos",null,valores)
            if(r == -1L){
                respuesta="Ocurrio un Error"
            }else{
                respuesta="Registro Exitoso"
            }
        }catch (e:Exception){
            respuesta=e.message.toString()
        }finally {
            db.close()
            }

        return respuesta

        }

        fun cargarProductos():ArrayList<Producto>{

            var listaProductos:ArrayList<Producto> = ArrayList()
            var query="SELECT * FROM productos"

            val db=base.readableDatabase

            val cursor:Cursor

            try {
                cursor=db.rawQuery(query,null)

                if(cursor.count>0){
                    cursor.moveToFirst()

                    do {
                        val id:Int=cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                        val nombre:String=cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                        val categoria:String=cursor.getString(cursor.getColumnIndexOrThrow("categoria"))
                        val precio:Double=cursor.getDouble(cursor.getColumnIndexOrThrow("precio"))
                        val stock:Int=cursor.getInt(cursor.getColumnIndexOrThrow("stock"))
                        val picture:ByteArray=cursor.getBlob(cursor.getColumnIndexOrThrow("picture"))

                        val producto=Producto()
                        producto.id=id
                        producto.nombre=nombre
                        producto.categoria=categoria
                        producto.precio=precio
                        producto.stock=stock
                        producto.picture=picture

                        listaProductos.add(producto)
                    }while (cursor.moveToNext())
                }
            }catch (e:Exception){
                Log.d("===",e.message.toString())
            }finally {
                db.close()
            }

            return listaProductos
        }




    }





