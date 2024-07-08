package com.temaApp.proyectofinal.adminSection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.temaApp.proyectofinal.adminSection.productsDB.CrearProductoActivity
import com.temaApp.proyectofinal.R
import com.temaApp.proyectofinal.autenticacion.LoginAdmin

import com.temaApp.proyectofinal.adminSection.productsDB.DeleteProduct
import com.temaApp.proyectofinal.adminSection.productsDB.ModifyProduct

import com.temaApp.proyectofinal.adminSection.viewProducts.ViewProducts
import com.temaApp.proyectofinal.autenticacion.usuarioAdmin

class DashAdmin : AppCompatActivity() {
    private lateinit var txtNombreUsuarioAdm: TextView
    private lateinit var btncrearproducto:AppCompatButton
    private lateinit var btncerrarsesion:Button
    private lateinit var btnModificarPage:AppCompatButton
    private lateinit var btnEliminarPage:AppCompatButton
    private lateinit var btnVerPage:AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_dash_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        asignar()
    }

    private fun asignar(){
        btncrearproducto=findViewById(R.id.btnCrearProducto)
        btncerrarsesion=findViewById(R.id.btnCerrarSesion)
        btnModificarPage=findViewById(R.id.btnModificarProducto)
        btnEliminarPage=findViewById(R.id.btnDeleteProducto)
        btnVerPage=findViewById(R.id.btnVerProducto)
        txtNombreUsuarioAdm=findViewById(R.id.txtNombreUsuarioAdm)
        txtNombreUsuarioAdm.text= usuarioAdmin.usuario?.usu_nombre

        btncrearproducto.setOnClickListener {
            val intent=Intent(this, CrearProductoActivity::class.java)
            startActivity(intent)
        }
        btnModificarPage.setOnClickListener {
            val intent=Intent(this, ModifyProduct::class.java)
            startActivity(intent)
        }
        btnEliminarPage.setOnClickListener {
            val intent=Intent(this, DeleteProduct::class.java)
            startActivity(intent)
        }
        btnVerPage.setOnClickListener {
            val intent=Intent(this, ViewProducts::class.java)
            startActivity(intent)
        }

        btncerrarsesion.setOnClickListener {
            //val intent2=Intent(this,LoginActivity::class.java)
            //startActivity(intent2)

            // Create the AlertDialog.Builder object
            val builder = AlertDialog.Builder(this)

            // Set the dialog title and message
            builder.setTitle("Confirmar cierre de sesión")
                .setMessage("¿Está seguro de que desea cerrar sesión?")

            // Set positive (Yes) button and its click listener
            builder.setPositiveButton("Sí") { _, _ ->
                // Close the current activity and start LoginActivity
                finish()
                val intent2 = Intent(this, LoginAdmin::class.java)
                startActivity(intent2)
            }

            // Set negative (No) button and its click listener
            builder.setNegativeButton("No") { dialog, _ ->
                // Do nothing if the user clicks "No"
                dialog.cancel()
            }

            // Create and show the AlertDialog
            val dialog = builder.create()
            dialog.show()
        }
    }
}