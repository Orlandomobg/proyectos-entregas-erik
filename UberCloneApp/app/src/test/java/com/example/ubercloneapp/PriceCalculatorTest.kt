package com.example.ubercloneapp


import org.junit.Assert.assertEquals
import org.junit.Test

// ═══════════════════════════════════════════
//  UNIT TEST — calcula en la JVM, sin Android
// ═══════════════════════════════════════════

class PriceCalculatorTest {

    // Función que queremos testear
    private fun calculatePrice(distanceKm: Double): Double {
        val baseFare   = 2.50
        val perKmRate  = 1.20
        val minFare    = 5.00
        val calculated = baseFare + (distanceKm * perKmRate)
        return maxOf(calculated, minFare)
    }

    @Test
    // ↑ @Test = "esta función es un test. Ejecútala al correr tests."
    fun `precio de viaje de 5km es correcto`() {
        // ↑ Nombre descriptivo con backticks. En tests se permite
        // usar espacios en el nombre para que sea legible.
        val price = calculatePrice(5.0)
        assertEquals(8.50, price, 0.01)
        // ↑ assertEquals(esperado, actual, delta)
        // delta = margen de error para doubles (por decimales).
        // 2.50 + (5 × 1.20) = 8.50 ✓
    }

    @Test
    fun `precio mínimo se aplica en viajes cortos`() {
        val price = calculatePrice(1.0)
        // 2.50 + (1 × 1.20) = 3.70 → pero el mínimo es 5.00
        assertEquals(5.00, price, 0.01)
    }

    @Test
    fun `distancia cero tiene tarifa mínima`() {
        val price = calculatePrice(0.0)
        assertEquals(5.00, price, 0.01)
    }
}