package com.temaApp.proyectofinal.compra


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.temaApp.proyectofinal.R
import com.temaApp.proyectofinal.autenticacion.usuarioGlobal
import com.temaApp.proyectofinal.clienteSection.historialpedidos.Historial
import com.temaApp.proyectofinal.clienteSection.miInbox.MiInbox
import com.temaApp.proyectofinal.clienteSection.profileDetails.Profile
import com.temaApp.proyectofinal.compra.carrito.Cart
import com.temaApp.proyectofinal.servicios.entidad.VentaRequest
import com.temaApp.proyectofinal.servicios.servicio.RetrofitClientTotal
import com.temaApp.proyectofinal.store.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CheckOut : AppCompatActivity() {

    private lateinit var btnRegistra_metodo: Button
    private lateinit var txtNumeroTarjeta: EditText
    private lateinit var txtCVV: EditText
    private lateinit var txtFechaVencimiento: EditText
    private lateinit var txtSubtotal: TextView
    private lateinit var txtIGV: TextView
    private lateinit var txtTotal: TextView
    private lateinit var btnmapa:Button
    private lateinit var store:ImageView
    private lateinit var cart:ImageView
    private lateinit var message:ImageView
    private lateinit var profile:ImageView
    private lateinit var lbdireccion:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_checkout)

        initComoponents()

        val spinnerTipoTarjeta: Spinner = findViewById(R.id.spinnerTipoTarjeta)
        val imageViewTarjeta: ImageView = findViewById(R.id.imageViewTarjeta)
        txtNumeroTarjeta = findViewById(R.id.txtNumeroTarjeta)
        txtCVV = findViewById(R.id.txtCVV)
        txtFechaVencimiento = findViewById(R.id.fechavenci)
        txtSubtotal = findViewById(R.id.subtotal)
        txtIGV = findViewById(R.id.igv)
        txtTotal = findViewById(R.id.total)

        // Configurar el botón para pagar
        btnRegistra_metodo = findViewById(R.id.btnRegistra_metodo)
        btnRegistra_metodo.setOnClickListener {
            if (validarCampos()) {
                CoroutineScope(Dispatchers.Main).launch {
                    pagarYVaciarCarrito()
                }
                showDialog()
            }
        }

        // Configurar Spinner de Tipo de Tarjeta
        val opciones = arrayOf("Visa", "Mastercard", "American Express", "Otros")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoTarjeta.adapter = adapter

        spinnerTipoTarjeta.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedTarjeta = opciones[position]
                actualizarImagenTarjeta(selectedTarjeta, imageViewTarjeta)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Agregar listener a campos de texto para validaciones dinámicas
        txtNumeroTarjeta.addTextChangedListener(textWatcher)
        txtCVV.addTextChangedListener(textWatcher)
        txtFechaVencimiento.addTextChangedListener(textWatcher)

        // Validar campos iniciales
        validarCampos()

        // Obtener el usu_id del usuario logueado
        val userId = usuarioGlobal.usuario?.usu_id ?: 0

        // Llamar a obtenerTotalesCarrito dentro de un coroutine
        CoroutineScope(Dispatchers.Main).launch {
            obtenerTotalesCarrito(userId)
        }
    }

    private fun initComoponents() {
        btnRegistra_metodo=findViewById(R.id.btnRegistra_metodo)
        store=findViewById(R.id.store)
        cart=findViewById(R.id.cart)
        message=findViewById(R.id.message)
        profile=findViewById(R.id.profile)
        btnmapa=findViewById(R.id.btnMap)
        lbdireccion = findViewById(R.id.lbdireccion)


        btnRegistra_metodo.setOnClickListener {
            showDialog()
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

        btnmapa.setOnClickListener{
            val intent = Intent(this, ActivityMap::class.java)
            startActivity(intent)

        }
        direccion()
    }

    private fun direccion() {
        val intent = intent.extras
        val direccion = intent?.getString("direccion")
        lbdireccion.text = direccion
    }


    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            validarCampos()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun validarCampos(): Boolean {
        val numeroTarjeta = txtNumeroTarjeta.text.toString().trim()
        val cvv = txtCVV.text.toString().trim()
        val fechaVencimiento = txtFechaVencimiento.text.toString().trim()

        val isNumeroTarjetaValido = numeroTarjeta.length == 16
        val isCVVValido = cvv.length in 3..4
        val isFechaVencimientoValida = validarFechaVencimiento(fechaVencimiento)

        // Actualizar estado del botón de pagar
        btnRegistra_metodo.isEnabled = isNumeroTarjetaValido && isCVVValido && isFechaVencimientoValida

        // Mostrar mensajes de error si los campos no son válidos
        //  txtNumeroTarjeta.error = if (numeroTarjeta.isEmpty()) "Ingrese número de tarjeta" else null
        //  txtCVV.error = if (cvv.isEmpty()) "Ingrese CVV" else null
        //  txtFechaVencimiento.error = if (fechaVencimiento.isEmpty()) "Ingrese fecha de vencimiento" else null

        return btnRegistra_metodo.isEnabled
    }

    private fun validarFechaVencimiento(fecha: String): Boolean {
        if (fecha.isEmpty()) return false

        val formatoFecha = SimpleDateFormat("MM/yy", Locale.getDefault())
        formatoFecha.isLenient = false

        try {
            val fechaIngresada = formatoFecha.parse(fecha)
            val fechaActual = Calendar.getInstance().time

            // Comparar la fecha ingresada con la fecha actual
            return fechaIngresada != null && fechaIngresada.after(fechaActual)
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.msgsuccess)
        val btnVerOrden: Button = dialog.findViewById(R.id.btn_ver_orden)
        val btnContinuarComprando: Button = dialog.findViewById(R.id.btn_continuar_compra)

        btnVerOrden.setOnClickListener {
            val intent = Intent(this, Historial::class.java)
            startActivity(intent)
        }
        btnContinuarComprando.setOnClickListener {
            val intent = Intent(this, Store::class.java)
            startActivity(intent)
        }
        dialog.show()
    }

    private fun actualizarImagenTarjeta(tarjeta: String, imageView: ImageView) {
        val drawableId = when (tarjeta) {
            "Visa" -> R.drawable.visa
            "Mastercard" -> R.drawable.mastercard
            "American Express" -> R.drawable.amex
            else -> R.drawable.default_tarjeta
        }
        imageView.setImageResource(drawableId)
    }

    private suspend fun obtenerTotalesCarrito(userId: Int) {
        val service = RetrofitClientTotal.web
        try {
            val response = service.getCarritoTotal(userId)
            if (response.isSuccessful) {
                val carritoTotal = response.body()
                if (carritoTotal != null) {
                    // Formatear los valores a dos decimales
                    val subtotalFormatted = String.format("%.2f", carritoTotal.subtotal)
                    val igvFormatted = String.format("%.2f", carritoTotal.igv)
                    val totalFormatted = String.format("%.2f", carritoTotal.total)

                    // Actualizar los TextViews con los datos obtenidos y formateados
                    txtSubtotal.text = subtotalFormatted
                    txtIGV.text = igvFormatted
                    txtTotal.text = totalFormatted
                }
            } else {
                // Manejar respuesta no exitosa
                Toast.makeText(applicationContext, "Error al obtener totales del carrito", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            // Manejar excepciones
            Toast.makeText(applicationContext, "Error: " + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun pagarYVaciarCarrito() {
        val userId = usuarioGlobal.usuario?.usu_id ?: return
        val service = RetrofitClientTotal.web
        try {
            val responseTotal = service.getCarritoTotal(userId)
            if (responseTotal.isSuccessful) {
                val carritoTotal = responseTotal.body()
                val total = carritoTotal?.total ?: 0.0

                val ventaRequest = VentaRequest(userId, total)
                val responseVenta = service.registrarVenta(ventaRequest)
                if (responseVenta.isSuccessful) {
                    Toast.makeText(applicationContext, "Venta realizada y carrito vaciado correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "Error al registrar la venta", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Error al obtener el total del carrito", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Error: " + e.message, Toast.LENGTH_SHORT).show()
        }
    }

}





