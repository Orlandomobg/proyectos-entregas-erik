package com.example.ubercloneapp.network


import retrofit2.http.GET
import retrofit2.http.Query

// ═══════════════════════════════════════════
//  INTERFAZ RETROFIT → Directions API
// ═══════════════════════════════════════════

interface DirectionsApi {

    // Obtener la ruta entre dos puntos
    @GET("directions/json")
    suspend fun getDirections(
        @Query("origin")      origin:      String,  // "lat,lng"
        @Query("destination") destination: String,  // "lat,lng"
        @Query("key")         key:         String,  // API Key
        @Query("mode")        mode:        String = "driving"
    ): DirectionsResponse
}

// ═══════════════════════════════════════════
//  MODELOS DE RESPUESTA (solo lo que necesitamos)
// ═══════════════════════════════════════════

data class DirectionsResponse(
    val routes: List<Route> = emptyList(),
    val status: String = ""
    // status: "OK", "NOT_FOUND", "ZERO_RESULTS", "REQUEST_DENIED"
)

data class Route(
    val legs: List<Leg> = emptyList(),
    val overview_polyline: OverviewPolyline = OverviewPolyline()
    // overview_polyline = la línea codificada de TODA la ruta
)

data class Leg(
    val distance: TextValue = TextValue(),
    // ↑ { "text": "12.3 km", "value": 12300 }  (value en METROS)
    val duration: TextValue = TextValue(),
    // ↑ { "text": "18 min",  "value": 1080 }   (value en SEGUNDOS)
    val start_address: String = "",
    val end_address:   String = ""
)

data class TextValue(
    val text:  String = "",  // Legible: "12.3 km"
    val value: Int    = 0     // Numérico: 12300
)

data class OverviewPolyline(
    val points: String = ""
    // String codificado con el algoritmo de Google
    // Se decodifica con decodePolyline() → List<LatLng>
)