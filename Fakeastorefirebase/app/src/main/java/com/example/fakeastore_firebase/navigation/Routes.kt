package com.example.fakeastore_firebase.navigation


// Cada pantalla tiene una "ruta" = un texto único que la identifica.
// Es como la URL de una página web, pero dentro de tu app.


object Routes {
    const val LOGIN    = "login"              // ← NUEVO
    const val REGISTER = "register"           // ← NUEVO
    const val CATALOG  = "catalog"
    const val DETAIL   = "detail/{productId}"
    const val CART     = "cart"

    fun detailRoute(productId: Int): String = "detail/$productId"
}
