package com.temaApp.proyectofinal.servicios.servicio

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.temaApp.proyectofinal.compra.CarritoTotalResponse
import com.temaApp.proyectofinal.servicios.ProImagenDeserializer
import com.temaApp.proyectofinal.servicios.entidad.Producto
import com.temaApp.proyectofinal.servicios.entidad.ProductoDtoGet
import com.temaApp.proyectofinal.servicios.entidad.Usuario
import com.temaApp.proyectofinal.servicios.entidad.UsuarioDTOActu
import com.temaApp.proyectofinal.servicios.entidad.UsuarioDTOChangeContra
import com.temaApp.proyectofinal.servicios.entidad.UsuarioDto
import com.temaApp.proyectofinal.servicios.entidad.VentaRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded

object AppConstantes {
    const val BASE_URL = "http://192.168.1.45:3000"
}

interface WebService {
    @Multipart
    @POST("/productos")
    suspend fun createProduct(
        @Part("pro_nombre") pro_nombre: RequestBody,
        @Part("pro_precio") pro_precio: RequestBody,
        @Part("pro_categoria") pro_categoria: RequestBody,
        @Part("pro_stock") pro_stock: RequestBody,
        @Part pro_imagen: MultipartBody.Part,
        @Part("estado") estado: RequestBody
    ): Response<CreateProductResponse>

    @POST("/login/cliente")
    suspend fun loginCliente(
        @Body usuarioDto: UsuarioDto
    ): Response<LoginClienteResponse>

    @POST("/login/admin")
    suspend fun loginAdmin(
        @Body usuarioDto: UsuarioDto
    ) :Response<LoginClienteResponse>


    @Multipart
    @PUT("/productos/{id}")
    suspend fun modifyProduct(
        @Path(value = "id") pro_id: Int,
        @Part("pro_nombre") pro_nombre: RequestBody,
        @Part("pro_precio") pro_precio: RequestBody,
        @Part("pro_categoria") pro_categoria: RequestBody,
        @Part("pro_stock") pro_stock: RequestBody,
        @Part pro_imagen: MultipartBody.Part,
        @Part("estado") estado: RequestBody
    ): Response<CreateProductResponse>

    @PUT("/productosDe/{id}")
    suspend fun deleteProduct(
        @Path(value = "id") pro_id: Int
    ): Response<CreateProductResponse>

    @GET("productos/{id}")
    suspend fun buscaProduct(@Path(value = "id") pro_id: Int): Response<ProductoBusquedaResponse>

    @GET("/productos")
    suspend fun getProducts(): Response<ProductoResponse>

    // Nueva función para agregar producto al carrito
    @FormUrlEncoded
    @POST("/carrito")
    suspend fun agregarProductoCarrito(
        @Field("pro_id") pro_id: Int,
        @Field("usu_id") usu_id: Int,
        @Field("cantidad") cantidad: Int
    ): Response<CreateProductResponse>

    @GET("/carrito/{usu_id}")
    suspend fun getProductsInCart(@Path("usu_id") userId: Int): Response<ProductoCartResponse>


    @DELETE("/carrito/{usu_id}/{pro_nombre}")
    suspend fun eliminarProductoDelCarrito(
        @Path("usu_id") usu_id: Int,
        @Path("pro_nombre") pro_nombre: String
    ): Response<JsonElement>

    @PUT("/changePass")
    suspend fun changePassword(
        @Body changePass:UsuarioDTOChangeContra
    ): Response<CreateProductResponse>

    @GET("/carritoTotal/{usu_id}")
    suspend fun getCarritoTotal(@Path("usu_id") userId: Int): Response<CarritoTotalResponse>

    // Eliminar todos los productos del carrito de un usuario
    @DELETE("/carrito/{usu_id}")
    suspend fun eliminarTodosProductosDelCarrito(
        @Path("usu_id") usu_id: Int
    ): Response<JsonElement>

    @PUT("/usuarios/{idBus}")
    suspend fun actualizarCliente(
        @Path("idBus") usu_id: Int?,
        @Body cliente: UsuarioDTOActu
    ):Response<CreateClienteResponse>

    @POST("/usuarios")
    suspend fun crearUsuarios(
        @Body cliente: Usuario
    ):Response<CreateClienteResponse>

    @POST("/registrarVenta")
    suspend fun registrarVenta(
        @Body ventaRequest: VentaRequest
    ): Response<JsonElement>

    @GET("/historial-ventas/{usuarioId}")
    suspend fun HistorialProducts(@Path(value = "usuarioId") usuarioId: Int): Response<HistorialResponse>

}

object RetrofitClientTotal {
    val web: WebService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConstantes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(WebService::class.java)
    }
}


object RetrofitClient {
    val web: WebService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConstantes.BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().create()
                )
            )
            .build().create(WebService::class.java)
    }
}

private val gson = GsonBuilder()
    .registerTypeAdapter(object : TypeToken<ByteArray>() {}.type, ProImagenDeserializer())
    .create()

object RetrofitClientImg {
    val webimg: WebService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConstantes.BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    gson
                )
            )
            .build().create(WebService::class.java)
    }
}


object RetrofitClientImgg {
    val webimg: WebService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConstantes.BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    gson
                )
            )
            .build().create(WebService::class.java)
    }
}
