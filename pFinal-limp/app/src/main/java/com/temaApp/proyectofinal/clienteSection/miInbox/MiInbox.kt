package com.temaApp.proyectofinal.clienteSection.miInbox

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.temaApp.proyectofinal.R
import com.temaApp.proyectofinal.clienteSection.profileDetails.Profile
import com.temaApp.proyectofinal.store.Store
import com.temaApp.proyectofinal.compra.carrito.Cart
import com.temaApp.proyectofinal.miInbox.MiInboxZcaja
import com.temaApp.proyectofinal.miInbox.miinboxAdapter

class MiInbox : AppCompatActivity() {
    private lateinit var boxHistorial: RecyclerView
    private lateinit var boxAdapter: miinboxAdapter
    private lateinit var store: ImageView
    private lateinit var cart: ImageView
    private lateinit var message: ImageView
    private lateinit var profile: ImageView


    private val alertmansaje = listOf(
        MiInboxZcaja("\nHa pagado correctamente su pedido\nORN-100001, ¡le entregaremos pronto! compruebe su \nbandeja de entrada para más información","22/06/2023 10:37 AM"),
        MiInboxZcaja("\nHa pagado correctamente su pedido\nORN-100002, ¡le entregaremos pronto! compruebe su \nbandeja de entrada para más información","24/06/2023 05:12 AM"),
        MiInboxZcaja("\nHa pagado correctamente su pedido\nORN-100003, ¡le entregaremos pronto! compruebe su \nbandeja de entrada para más información","26/06/2023 02:19 PM"),
        MiInboxZcaja("\nHa pagado correctamente su pedido\nORN-100004, ¡le entregaremos pronto! compruebe su \nbandeja de entrada para más información","28/06/2023 12:48 AM"),
        MiInboxZcaja("\nHa pagado correctamente su pedido\nORN-100005, ¡le entregaremos pronto! compruebe su \nbandeja de entrada para más información","30/06/2023 06:21 AM"),
        MiInboxZcaja("\nHa pagado correctamente su pedido\nORN-100006, ¡le entregaremos pronto! compruebe su \nbandeja de entrada para más información","02/07/2023 08:55 PM"),
        MiInboxZcaja("\nHa pagado correctamente su pedido\nORN-100007, ¡le entregaremos pronto! compruebe su \nbandeja de entrada para más información","04/07/2023 04:47 PM"),
        MiInboxZcaja("\nHa pagado correctamente su pedido\nORN-100008, ¡le entregaremos pronto! compruebe su \nbandeja de entrada para más información","06/07/2023 11:10 PM")

    )





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mi_inbox)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initComoponents()
        iniMENSAje()
    }


    private fun initComoponents() {
        boxHistorial=findViewById(R.id.boxHistorial)
        store=findViewById(R.id.store)
        cart=findViewById(R.id.cart)
        message=findViewById(R.id.message)
        profile=findViewById(R.id.profile)



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
    private fun iniMENSAje(){
        boxAdapter = miinboxAdapter(alertmansaje)
        boxHistorial.layoutManager= GridLayoutManager(this,1)
        boxHistorial.adapter= boxAdapter

    }
}