package com.example.uber.navigation

object Routes {
    const val ONBOARDING = "onboarding" // Tu pantalla azul inicial
    const val LOGIN      = "login"

    const val EMAIL      = "loginEmail"
    const val REGISTER    = "register"
    const val OTP        = "otp"
    const val SAFETY = "safetyAlert"
    const val HOME = "home"
    const val PROFILE = "profile"

    const val MAP         = "map"

    const val REQUEST     = "request_ride"   // Elegir destino
    const val IN_PROGRESS = "ride_progress"  // Viaje en curso
    const val PAYMENT     = "payment"       // Pago con Stripe ← NUEVO
    const val HISTORY     = "ride_history"   // Historial
    const val RIDE_DETAIL = "ride_detail/{rideId}"


}

