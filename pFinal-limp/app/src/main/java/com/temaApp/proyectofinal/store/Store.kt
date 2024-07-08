package com.temaApp.proyectofinal.store

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ImageSpan
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.temaApp.proyectofinal.R
import com.temaApp.proyectofinal.clienteSection.miInbox.MiInbox
import com.temaApp.proyectofinal.clienteSection.profileDetails.Profile
import com.temaApp.proyectofinal.servicios.entidad.ProductoDtoGet
import com.temaApp.proyectofinal.servicios.servicio.RetrofitClientImg
import com.temaApp.proyectofinal.store.categorias.ProductCategory.*
import com.temaApp.proyectofinal.compra.carrito.Cart
import com.temaApp.proyectofinal.store.categorias.CategoriesAdapter
import com.temaApp.proyectofinal.store.productos.ProductAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Store : AppCompatActivity() {


    private lateinit var rvCategories:RecyclerView
    private lateinit var categoriesAdapter: CategoriesAdapter

    private lateinit var rvProducts:RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var etBuscar: EditText

    //menu
    private lateinit var cart:ImageView
    private lateinit var messager:ImageView
    private lateinit var profile:ImageView


    private val categories= listOf(

        Cupcake,
        Postres,
        Panes,
        Otros

    )

    private lateinit var listaProductos:MutableList<ProductoDtoGet>
    private lateinit var productListFilter:MutableList<ProductoDtoGet>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_store)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initComponents()
        cargarDatos()
        initUi()
        agregaIcono()
    }

    private fun initComponents(){
        rvCategories=findViewById(R.id.pCategorias)
        rvProducts=findViewById(R.id.productosDes)
        cart=findViewById(R.id.cart)
        messager=findViewById(R.id.message)
        profile=findViewById(R.id.profile)
        etBuscar=findViewById(R.id.etBuscar)

        cart.setOnClickListener {
            val intent=Intent(this, Cart::class.java)
            startActivity(intent)
        }

        messager.setOnClickListener {
            val intent=Intent(this, MiInbox::class.java)
            startActivity(intent)
        }
        profile.setOnClickListener {
            val intent=Intent(this, Profile::class.java)
            startActivity(intent)
        }

    }

    private fun cargarDatos(){

        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetrofitClientImg.webimg.getProducts()
            runOnUiThread {
                if(rpta.isSuccessful){
                    listaProductos = rpta.body()!!.listarProductos
                    productAdapter= ProductAdapter(listaProductos)
                    mostrarDatos()

                }
            }
        }
    }
    private fun  mostrarDatos(){
        rvProducts.adapter=productAdapter

        updateTasks()
        etBuscar.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                updateBusqueda(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })


    }


    private fun initUi(){

        categoriesAdapter= CategoriesAdapter(categories){ position -> updateCategories(position)}
        rvCategories.layoutManager=LinearLayoutManager(
            this,LinearLayoutManager.HORIZONTAL,false
        )
        rvCategories.adapter=categoriesAdapter



        rvProducts.layoutManager=GridLayoutManager(this,2)





    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateCategories(position: Int){

        categories.forEachIndexed { index, category ->
            category.isSelected = index == position
        }
        categoriesAdapter.notifyDataSetChanged()
        updateTasks()


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateTasks(){
        val selectedCategories: List<String> = categories.filter { it.isSelected }.map { it.getCategoryName() }

        productListFilter= listaProductos.filter { selectedCategories.contains(it.pro_categoria) }.toMutableList()
        productAdapter.filteredProductos=productListFilter

        productAdapter.notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateBusqueda(query:String){

        val selectedCategories: List<String> = categories.filter { it.isSelected }.map { it.getCategoryName() }

        productListFilter= listaProductos.filter { selectedCategories.contains(it.pro_categoria) }.toMutableList()

        productAdapter.filteredProductos = if (query.isEmpty()) {
            productListFilter
        } else {
            productListFilter.filter { it.pro_nombre.contains(query, ignoreCase = true) }.toMutableList()
        }

        productAdapter.notifyDataSetChanged()


    }


    private fun agregaIcono() {
        val etBuscar = etBuscar

        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_search)
        drawable?.let {
            // Ajustar el tamaño del ícono
            val newWidth = (it.intrinsicWidth * 0.4).toInt()  // Ajusta el factor de escala según tu preferencia
            val newHeight = (it.intrinsicHeight * 0.4).toInt()
            it.setBounds(0, 0, newWidth, newHeight)

            // Construir el texto con el ícono al final
            val text = "Buscar Productos"
            val hintWithIcon = SpannableStringBuilder(text).apply {
                append("                                 ")
                setSpan(ImageSpan(it), length - 1, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            // Establecer el texto con el ícono en el hint del EditText
            etBuscar.hint = hintWithIcon
        }
    }



}