package com.temaApp.proyectofinal.modeloVistaControlador.util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatos(context:Context): SQLiteOpenHelper(context,DATABASE_NAME,null,VERSION) {

    companion object{
        private const val DATABASE_NAME = "bakery.db"
        private const val VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {

        val sql = "CREATE TABLE IF NOT EXISTS productos" +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " nombre TEXT NOT NULL, " +
                " categoria TEXT NOT NULL, " +
                " precio REAL NOT NULL," +
                " stock INTEGER NOT NULL," +
                " picture BLOB NOT NULL);"

        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS productos")
        onCreate(db)
    }
}