package com.temaApp.proyectofinal.clienteSection

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.imageview.ShapeableImageView
import com.temaApp.proyectofinal.R
import com.temaApp.proyectofinal.autenticacion.usuarioGlobal
import com.temaApp.proyectofinal.clienteSection.profileDetails.Profile


class DetalleCuenta : AppCompatActivity() {
    private lateinit var btnvolver:ImageView
    private lateinit var btneditar:Button
    private lateinit var txtnombre:TextView
    private lateinit var txtapellido:TextView
    private lateinit var txtcorreo:TextView
    private lateinit var txtcelular:TextView
    private lateinit var cambiarimagen:ImageView

    //nuevo
    private lateinit var txtcontra:TextView

    private lateinit var txtusuario:TextView

    //usuarios global id
    val Usuarioss = usuarioGlobal.usuario

    lateinit var imageBytes:ByteArray

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){uri ->
        if(uri!=null){
            cambiarimagen.setImageURI(uri)
            val inputStream = contentResolver.openInputStream(uri)
            if (inputStream!= null) {
                imageBytes = inputStream.readBytes()
            }
            inputStream?.close()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_cuenta)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        asignar()
    }

    private fun asignar(){
        btneditar = findViewById(R.id.btnDCeditar)
        txtnombre=findViewById(R.id.txtDCnombre)
        txtapellido=findViewById(R.id.txtDCapellido)
        txtcorreo = findViewById(R.id.txtDCcorreo)
        txtcelular=findViewById(R.id.txtDCcelular)
        btnvolver = findViewById(R.id.btnDCvolver)
        cambiarimagen=findViewById(R.id.DCimagen)

        txtusuario=findViewById(R.id.txtDCusuario)


        //nuevo
        txtcontra=findViewById(R.id.txtDcontra)
        txtcontra.visibility= View.GONE

        colocardatos()
        btneditar.setOnClickListener {
            val intent = Intent(this,CrearCliente::class.java)
            intent.putExtra("ocultarContraseña",true)
            intent.putExtra("nombre",txtnombre.text.toString())
            intent.putExtra("apellido",txtapellido.text.toString())
            intent.putExtra("correo",txtcorreo.text.toString())
            intent.putExtra("celular",txtcelular.text.toString())
            intent.putExtra("contraseña",txtcontra.text.toString())
            startActivity(intent)
        }

        btnvolver.setOnClickListener {
            val intent= Intent(this,Profile::class.java)

            startActivity(intent)
        }

        cambiarimagen.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun colocardatos() {
        // Asignar los valores recibidos de la variable global a los TextViews
        usuarioGlobal.usuario?.let {
            txtnombre.text=it.usu_nombre
            txtapellido.text=it.usu_apellidos
            txtcorreo.text=it.usu_email
            txtcelular.text=it.usu_ncelular
            txtcontra.text=it.usu_password

            txtusuario.text= it.usu_nombre +" "+it.usu_apellidos
        }
    }
}