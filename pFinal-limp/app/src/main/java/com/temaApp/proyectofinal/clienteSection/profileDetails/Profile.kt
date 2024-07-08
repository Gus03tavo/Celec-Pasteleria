package com.temaApp.proyectofinal.clienteSection.profileDetails

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.temaApp.proyectofinal.MainActivity
import com.temaApp.proyectofinal.R
import com.temaApp.proyectofinal.autenticacion.usuarioGlobal
import com.temaApp.proyectofinal.clienteSection.DetalleCuenta
import com.temaApp.proyectofinal.clienteSection.historialpedidos.Historial
import com.temaApp.proyectofinal.clienteSection.miInbox.MiInbox
import com.temaApp.proyectofinal.store.Store
import com.temaApp.proyectofinal.compra.carrito.Cart
import com.temaApp.proyectofinal.clienteSection.Policy
import org.w3c.dom.Text

class Profile : AppCompatActivity() {

    private lateinit var orderHistory:LinearLayout
    private lateinit var privacyPolicy:LinearLayout
    private lateinit var detalleCuenta:LinearLayout
    private lateinit var store: ImageView
    private lateinit var cart: ImageView
    private lateinit var message: ImageView
    private lateinit var profile: ImageView
    private lateinit var txtusuario:TextView

    private lateinit var btnCerrarSesion:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_producto_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        iniComponentes()
    }

    private fun iniComponentes(){
        orderHistory=findViewById(R.id.orderHistory)
        privacyPolicy=findViewById(R.id.privacyPolicy)
        detalleCuenta=findViewById(R.id.detalleCuenta)
        store=findViewById(R.id.store)
        cart=findViewById(R.id.cart)
        message=findViewById(R.id.message)
        profile=findViewById(R.id.profile)
        btnCerrarSesion=findViewById(R.id.btnCerrarSesion)
        txtusuario=findViewById(R.id.txtusuario)
        cerrarSesion()
        nonbreUsuario()
        orderHistory.setOnClickListener{
            val intent=Intent(this, Historial::class.java)
            startActivity(intent)
        }
        privacyPolicy.setOnClickListener{
            val intent=Intent(this,Policy::class.java)
            startActivity(intent)
        }

        detalleCuenta.setOnClickListener{
            val intent=Intent(this,DetalleCuenta::class.java)
            startActivity(intent)
        }

        store.setOnClickListener{
            val intent = Intent(this, Store::class.java)
            startActivity(intent)
        }
        cart.setOnClickListener{
            val intent = Intent(this, Cart::class.java)
            startActivity(intent)
        }
        message.setOnClickListener{
            val intent = Intent(this, MiInbox::class.java)
            startActivity(intent)
        }
        profile.setOnClickListener{
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }


    }

    private fun cerrarSesion(){
        btnCerrarSesion.setOnClickListener {
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
                val intent2 = Intent(this, MainActivity::class.java)
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

    private fun nonbreUsuario() {
        usuarioGlobal.usuario?.let {
            txtusuario.text = it.usu_nombre +" "+it.usu_apellidos
        }
    }
}