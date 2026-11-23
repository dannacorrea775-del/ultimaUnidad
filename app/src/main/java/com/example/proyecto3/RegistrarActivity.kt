package com.example.proyecto3

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto3.databinding.ActivityRegistrarBinding
import com.google.firebase.firestore.FirebaseFirestore

class RegistrarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarBinding
    private val db by lazy { FirebaseFirestore.getInstance() }
    private var productoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productoId = intent.getStringExtra("productoId")
        val nombre = intent.getStringExtra("nombre")
        val categoria = intent.getStringExtra("categoria")
        val cantidad = intent.getIntExtra("cantidad", 0)
        val precio = intent.getDoubleExtra("precio", 0.0)
        val descripcion = intent.getStringExtra("descripcion")

        if (!productoId.isNullOrEmpty()) {
            binding.tvTitulo.text = "Editar producto"
            binding.btnGuardar.text = "Guardar cambios"
            binding.etNombre.setText(nombre ?: "")
            binding.etCategoria.setText(categoria ?: "")
            binding.etCantidad.setText(cantidad.toString())
            binding.etPrecio.setText(precio.toString())
            binding.etDescripcion.setText(descripcion ?: "")
        }

        binding.btnGuardar.setOnClickListener {
            guardarProducto()
        }

        binding.btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun guardarProducto() {
        val nombre = binding.etNombre.text.toString().trim()
        val categoria = binding.etCategoria.text.toString().trim()
        val cantidadStr = binding.etCantidad.text.toString().trim()
        val precioStr = binding.etPrecio.text.toString().trim()
        val descripcion = binding.etDescripcion.text.toString().trim()

        if (nombre.isEmpty() || categoria.isEmpty() || cantidadStr.isEmpty() || precioStr.isEmpty()) {
            Toast.makeText(this, "Por favor completa los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val cantidad = cantidadStr.toIntOrNull() ?: 0
        val precio = precioStr.toDoubleOrNull() ?: 0.0

        val datosProducto = hashMapOf(
            "nombre" to nombre,
            "categoria" to categoria,
            "cantidad" to cantidad,
            "precio" to precio,
            "descripcion" to descripcion
        )

        if (!productoId.isNullOrEmpty()) {
            db.collection("productos").document(productoId!!)
                .set(datosProducto)
                .addOnSuccessListener {
                    Toast.makeText(this, "Producto actualizado correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al actualizar: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
        } else {
            db.collection("productos")
                .add(datosProducto)
                .addOnSuccessListener {
                    Toast.makeText(this, "Producto registrado correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al registrar: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
