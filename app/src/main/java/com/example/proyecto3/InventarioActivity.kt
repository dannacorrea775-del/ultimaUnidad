
package com.example.proyecto3

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto3.databinding.ActivityInventarioBinding
import com.google.firebase.firestore.FirebaseFirestore

class InventarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInventarioBinding
    private val db = FirebaseFirestore.getInstance()
    private val productos = mutableListOf<Producto>()
    private lateinit var adapter: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInventarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ProductoAdapter(
            productos,
            { producto ->
                val intent = Intent(this, RegistrarActivity::class.java)
                intent.putExtra("productoId", producto.id)
                intent.putExtra("nombre", producto.nombre)
                intent.putExtra("categoria", producto.categoria)
                intent.putExtra("cantidad", producto.cantidad)
                intent.putExtra("precio", producto.precio)
                intent.putExtra("descripcion", producto.descripcion)
                startActivity(intent)
            },
            { producto ->
                eliminarProducto(producto)
            }
        )

        binding.rvProductos.layoutManager = LinearLayoutManager(this)
        binding.rvProductos.adapter = adapter

        binding.btnVolver.setOnClickListener {
            finish()
        }

        cargarProductos()
    }

    private fun cargarProductos() {
        db.collection("productos").addSnapshotListener { datos, error ->

            if (error != null) {
                Toast.makeText(this, "Error al cargar", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            if (datos != null) {
                productos.clear()

                for (documento in datos.documents) {
                    val producto = documento.toObject(Producto::class.java)

                    if (producto != null) {
                        producto.id = documento.id
                        productos.add(producto)
                    }
                }

                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun eliminarProducto(producto: Producto) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar producto")
            .setMessage("¿Deseas eliminar ${producto.nombre}?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->
                db.collection("productos")
                    .document(producto.id)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(
                            this,"Producto eliminado",Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "No se pudo eliminar", Toast.LENGTH_SHORT).show()
                    }
            }
            .show()
    }
}
