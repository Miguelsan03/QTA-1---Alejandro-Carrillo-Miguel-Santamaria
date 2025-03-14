package com.example.qta

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val campoValorPropiedad = findViewById<EditText>(R.id.valorPropiedad)
        val campoValorPrestamo = findViewById<EditText>(R.id.valorPrestamo)
        val campoPlazoAnos = findViewById<EditText>(R.id.plazoAnos)
        val campoTae = findViewById<EditText>(R.id.tae)
        val resultado = findViewById<TextView>(R.id.resultado)
        val simular = findViewById<Button>(R.id.simular)

        fun calcularCuota(valorPrestamo: Int, plazoAnos: Int, tae: Double) {
            val plazoMeses = plazoAnos * 12
            val taeMes = (tae / 12) / 100

            if (taeMes > 0) {
                val factor = (1 + taeMes).pow(plazoMeses.toDouble())
                val cuota = valorPrestamo * (factor * taeMes) / (factor - 1)

                resultado.text = "Paga cuotas de $${String.format("%,.0f", cuota)} por mes"
            } else {
                resultado.text = "Error: La tasa de interés debe ser mayor a 0."
            }
        }

        fun validarCampos() {
            val stringPropiedad = campoValorPropiedad.text.toString()
            val stringPrestamo = campoValorPrestamo.text.toString()
            val stringPlazo = campoPlazoAnos.text.toString()
            val stringTae = campoTae.text.toString()

            if (stringPropiedad.isEmpty() || stringPrestamo.isEmpty() || stringPlazo.isEmpty() || stringTae.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_LONG).show()
                return
            }

            val valorPropiedad = stringPropiedad.toInt()
            val valorPrestamo = stringPrestamo.toInt()
            val plazoAnos = stringPlazo.toInt()
            val tae = stringTae.toDouble()

            // Validaciones de rango
            if (valorPropiedad < 70_000_000) {
                Toast.makeText(this, "El valor de la propiedad debe ser al menos $70,000,000", Toast.LENGTH_LONG).show()
                return
            }

            if (valorPrestamo < 50_000_000) {
                Toast.makeText(this, "El valor del préstamo debe ser al menos $50,000,000", Toast.LENGTH_LONG).show()
                return
            }

            if (plazoAnos !in 5..20) {
                Toast.makeText(this, "El plazo debe estar entre 5 y 20 años", Toast.LENGTH_LONG).show()
                return
            }

            calcularCuota(valorPrestamo, plazoAnos, tae)
        }

        simular.setOnClickListener {
            validarCampos()
        }
    }
}
