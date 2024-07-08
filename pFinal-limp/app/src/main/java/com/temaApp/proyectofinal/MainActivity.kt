package com.temaApp.proyectofinal

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.temaApp.proyectofinal.autenticacion.LoginAdmin
import com.temaApp.proyectofinal.clienteSection.CrearCliente
import com.temaApp.proyectofinal.autenticacion.LoginCliente

class MainActivity : AppCompatActivity() {
    private lateinit var btnLogin: Button
    private lateinit var btnAdmin:Button
    private lateinit var btnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initComponents()
        permisosMap()
    }

    private fun permisosMap(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                123
            )
        }
    }


    private fun initComponents(){

        btnLogin=findViewById(R.id.btnLogin)
        btnAdmin=findViewById(R.id.btnAdmin)
        btnSignUp=findViewById(R.id.btnSignUp)

        btnLogin.setOnClickListener {
            val intent= Intent(this, LoginCliente::class.java)
            startActivity(intent)
        }

        btnAdmin.setOnClickListener {
            val intent= Intent(this, LoginAdmin::class.java)
            startActivity(intent)
        }

        btnSignUp.setOnClickListener {
            val intent= Intent(this, CrearCliente::class.java)
            startActivity(intent)
        }
    }




}