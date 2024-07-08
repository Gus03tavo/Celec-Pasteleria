package com.temaApp.proyectofinal.clienteSection

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.temaApp.proyectofinal.MainActivity
import com.temaApp.proyectofinal.R
import com.temaApp.proyectofinal.autenticacion.LoginCliente
import com.temaApp.proyectofinal.autenticacion.usuarioGlobal
import com.temaApp.proyectofinal.servicios.entidad.Usuario
import com.temaApp.proyectofinal.servicios.entidad.UsuarioDTOActu
import com.temaApp.proyectofinal.servicios.servicio.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CrearCliente : AppCompatActivity() {
    private lateinit var btnBack : ImageView
    private lateinit var tvTerminos : TextView
    private lateinit var tvPoliticas : TextView
    private lateinit var tvInicioSesion : TextView

    private lateinit var txtnombres: EditText
    private lateinit var txtapellidos: EditText
    private lateinit var txtemail: EditText
    private lateinit var txtcelular: EditText
    private lateinit var txtpassword: EditText
    private lateinit var txtconfirmarpwd: EditText

    private lateinit var lbcontra:TextView
    private lateinit var lbconfirmar:TextView
    private lateinit var lbtitulo:TextView
    private lateinit var lbsubtitulo:TextView
    private lateinit var lbask:TextView
    private lateinit var btnregistrar:Button


    //usuarios global id
    var id_Usuario = usuarioGlobal.usuariid

    private var seModifica:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crearcliente)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initComponents()
        Log.d( "===", id_Usuario.toString())
    }

    fun initComponents(){
        tvTerminos = findViewById(R.id.tvTerminos)
        tvPoliticas = findViewById(R.id.tvPoliticas)
        tvInicioSesion = findViewById(R.id.tvInicioSesion)
        btnBack = findViewById(R.id.btnBack)
        btnregistrar = findViewById(R.id.btnCCregistrarse)
        txtnombres = findViewById(R.id.txtCCnombres)
        txtapellidos = findViewById(R.id.txtCCapellidos)
        txtemail = findViewById(R.id.txtCCemail)
        txtcelular = findViewById(R.id.txtCCcelular)
        txtpassword = findViewById(R.id.txtCCcontra)
        txtconfirmarpwd = findViewById(R.id.txtCCconfirmar)

        ocultar()
        val subrayado1 = SpannableString(tvTerminos.text)
        subrayado1.setSpan(UnderlineSpan(),0,tvTerminos.text.length,0)

        val subrayado2 = SpannableString(tvPoliticas.text)
        subrayado2.setSpan(UnderlineSpan(),0,tvPoliticas.text.length,0)

        val subrayado3 = SpannableString(tvInicioSesion.text)
        subrayado3.setSpan(UnderlineSpan(),0,tvInicioSesion.text.length,0)

        tvTerminos.text = subrayado1
        tvPoliticas.text = subrayado2
        tvInicioSesion.text=subrayado3

        btnregistrar.setOnClickListener {
            if(seModifica){
                editarCliente()
            }else{
                crearCliente()
            }
        }

        btnBack.setOnClickListener {
            if (seModifica){
                val intent= Intent(this,DetalleCuenta::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }

        }

        tvInicioSesion.setOnClickListener {
            val intent = Intent(this, LoginCliente::class.java)
            startActivity(intent)
        }

        tvTerminos.setOnClickListener {
            val intent = Intent(this,Terms::class.java)
            startActivity(intent)
        }

        tvPoliticas.setOnClickListener {
            val intent = Intent(this,Policy::class.java)
            startActivity(intent)
        }
    }

    private fun editarCliente() {

        val nombres = txtnombres.text.toString()
        val apellidos = txtapellidos.text.toString()
        val email = txtemail.text.toString()
        val celular = txtcelular.text.toString()


        // Guarda el correo electrónico actual antes de actualizarlo
        val correoAnterior = usuarioGlobal.usuario?.usu_email

        usuarioGlobal.usuario?.usu_nombre=nombres
        usuarioGlobal.usuario?.usu_apellidos=apellidos
        usuarioGlobal.usuario?.usu_email=email
        usuarioGlobal.usuario?.usu_ncelular=celular

        val cliente = UsuarioDTOActu(nombres, apellidos, email, celular)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = cliente.let { RetrofitClient.web.actualizarCliente(id_Usuario, cliente) }
                withContext(Dispatchers.Main){
                    if(response.isSuccessful){
                        val mensaje = response.body()
                        mensaje?.let {
                            if(correoAnterior!=email){
                                cerrarsesion()
                            }else{
                                mensajitoEditar(it.message)
                            }

                        }
                    }else{
                        Toast.makeText(this@CrearCliente, response.body().toString(), Toast.LENGTH_SHORT).show()
                        Log.d("====",response.body().toString())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CrearCliente, "Error del servidor: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.d("====", e.message.toString())
                }
            }
        }
    }

    private fun cerrarsesion() {
        // Limpiar preferencias compartidas
        val preferences = getSharedPreferences("nombre_de_tu_preference", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.clear()
        editor.apply()

        // Mostrar mensaje de iniciar sesión nuevamente
        Toast.makeText(this@CrearCliente, "Usuario Editador Correctamente, "+"\n"+"Por favor, inicia sesión nuevamente.", Toast.LENGTH_LONG).show()

        // Redirigir al usuario a la pantalla de inicio de sesión
        val intent = Intent(this, LoginCliente::class.java)
        startActivity(intent)

        // Finaliza esta actividad para que el usuario no pueda volver atrás
        finish()
    }

    private fun crearCliente() {
        val nombres = txtnombres.text.toString()
        val apellidos = txtapellidos.text.toString()
        val email = txtemail.text.toString()
        val celular = txtcelular.text.toString()
        val password = txtpassword.text.toString()
        val confirmar = txtconfirmarpwd.text.toString()


        // Validar que todos los campos estén completos y realizar todas las validaciones en un solo condicional
        if (nombres.isEmpty() || apellidos.isEmpty() || email.isEmpty() || celular.isEmpty() || password.isEmpty() || confirmar.isEmpty()) {
            if (nombres.isEmpty()) txtnombres.error = "Campo obligatorio"
            if (apellidos.isEmpty()) txtapellidos.error = "Campo obligatorio"
            if (email.isEmpty()) txtemail.error = "Campo obligatorio"
            if (celular.isEmpty()) txtcelular.error = "Campo obligatorio"
            if (password.isEmpty()) txtpassword.error = "Campo obligatorio"
            if (confirmar.isEmpty()) txtconfirmarpwd.error = "Campo obligatorio"
        } else {
            if (celular.length != 9) txtcelular.error = "El celular debe tener 9 dígitos"
            //if (!email.matches(Regex("^\\w+@[a-zA-Z]+\\.(gmail\\.com|upn\\.pe|hotmail\\.com)\$"))) txtemail.error = "Email inválido"
            if (password != confirmar) {
                txtpassword.error = "Las contraseñas no coinciden"
                txtconfirmarpwd.error = "Las contraseñas no coinciden"
            }
            if (nombres.matches(Regex(".*\\d+.*"))) txtnombres.error = "No debe contener números"
            if (apellidos.matches(Regex(".*\\d+.*"))) txtapellidos.error = "No debe contener números"
            // Si todas las validaciones pasan, llamar a la función crear() para crear el cliente
            if (txtnombres.error.isNullOrEmpty() && txtapellidos.error.isNullOrEmpty() && txtemail.error.isNullOrEmpty() && txtcelular.error.isNullOrEmpty() && txtpassword.error.isNullOrEmpty() && txtconfirmarpwd.error.isNullOrEmpty()) {
                val cliente = Usuario(
                    1,
                    nombres,
                    apellidos,
                    email,
                    celular,
                    password,
                    "1"
                )
                CoroutineScope(Dispatchers.IO).launch {
                    val rpta =RetrofitClient.web.crearUsuarios(cliente)
                    withContext(Dispatchers.Main){
                        if(rpta.isSuccessful){
                            val respuesta = rpta.body()
                            respuesta?.let {
                                mensajito(it.message)
                            }
                        }else{
                            Toast.makeText(this@CrearCliente, rpta.body().toString(), Toast.LENGTH_SHORT).show()
                            Log.d("====",rpta.body().toString())
                        }
                    }
                }
            }
        }
    }

    private fun ocultar() {
        lbcontra = findViewById(R.id.lbCCcontra)
        lbconfirmar = findViewById(R.id.lbCCconfirmar)
        lbtitulo = findViewById(R.id.lbCCtitulo)
        lbsubtitulo = findViewById(R.id.lbCCsubtitulo)
        lbask = findViewById(R.id.lbCCcuentaask)

        val ocultarContra = intent.getBooleanExtra("ocultarContraseña", false)
        if (ocultarContra){
            seModifica = true
            txtpassword.visibility = View.GONE
            txtconfirmarpwd.visibility = View.GONE
            lbcontra.visibility = View.GONE
            lbconfirmar.visibility = View.GONE
            lbask.visibility = View.GONE
            tvInicioSesion.visibility = View.GONE
            lbtitulo.text = "Cambiar Datos Usuario"
            lbsubtitulo.text = "Ingrese los datos de su cuenta"
            btnregistrar.text = "Cambiar Datos"

            //obtener los margin del titulo y subtitulo
            val mastitulo = lbtitulo.layoutParams as ViewGroup.MarginLayoutParams
            val masubtitulo = lbsubtitulo.layoutParams as ViewGroup.MarginLayoutParams

            //modifico los margenes
            val margenLeft = 32
            val margenTop = 10
            val submarginLeft = 12
            val escala = resources.displayMetrics.density
            val nuevoLeftMargin= (margenLeft * escala + 0.5f).toInt()
            val nuevoTopMargin = (margenTop * escala + 0.5f).toInt()
            val nuevoSubMarginLeft = (submarginLeft * escala + 0.5f).toInt()

            mastitulo.setMargins(nuevoLeftMargin,nuevoTopMargin,0,0)
            masubtitulo.setMargins(nuevoSubMarginLeft,0,0,0)


            txtnombres.setText(intent.getStringExtra("nombre"))
            txtapellidos.setText(intent.getStringExtra("apellido"))
            txtemail.setText(intent.getStringExtra("correo"))
            txtcelular.setText(intent.getStringExtra("celular"))
            txtpassword.setText(intent.getStringExtra("contraseña"))

        }

    }

    private fun mensajito(mensaje: String) {
        val ventana = AlertDialog.Builder(this)
        ventana.setTitle("Mensaje Informativo")
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar", DialogInterface.OnClickListener{ dialog, which ->
            val intent = Intent(this,LoginCliente::class.java)
            startActivity(intent)
        })
        ventana.create().show()

    }

    private fun mensajitoEditar(mensaje: String) {
        val ventana = AlertDialog.Builder(this)
        ventana.setTitle("Mensaje Informativo")
        ventana.setMessage(mensaje)
        //ventana.setPositiveButton("aceptar",null)
        ventana.setPositiveButton("Aceptar", DialogInterface.OnClickListener{ dialog, which ->
            val intent = Intent(this,DetalleCuenta::class.java)
            startActivity(intent)
        })
        ventana.create().show()
    }
}