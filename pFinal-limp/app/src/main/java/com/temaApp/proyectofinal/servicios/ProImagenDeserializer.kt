package com.temaApp.proyectofinal.servicios

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ProImagenDeserializer : JsonDeserializer<ByteArray> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ByteArray {
        val images = mutableListOf<Byte>()
        json?.let {
            if (it.isJsonObject) {
                val dataArray = it.asJsonObject.getAsJsonArray("data")
                dataArray?.forEach { byteElement ->
                    images.add(byteElement.asByte)
                }
            }
        }
        return images.toByteArray()
    }
}