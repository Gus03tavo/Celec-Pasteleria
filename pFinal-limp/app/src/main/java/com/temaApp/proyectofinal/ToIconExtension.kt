package com.temaApp.proyectofinal

import android.graphics.BitmapFactory
import android.graphics.drawable.Icon

fun ByteArray.toIcon(): Icon {
    val bitmap = BitmapFactory.decodeByteArray(this, 0, this.size)
    return Icon.createWithBitmap(bitmap)
}
