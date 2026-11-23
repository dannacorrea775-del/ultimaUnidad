
package com.example.proyecto3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto3.databinding.ItemProductoBinding

class ProductoAdapter(
    private val productos: MutableList<Producto>,
    private val editar: (Producto) -> Unit,
    private val eliminar: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(
        val binding: ItemProductoBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductoViewHolder {
        val binding = ItemProductoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ProductoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        val binding = holder.binding

        binding.tvNombre.text = producto.nombre
        binding.tvCategoria.text = "Categoría: ${producto.categoria}"
        binding.tvCantidad.text = "Cantidad: ${producto.cantidad}"
        binding.tvPrecio.text = "Precio: $${producto.precio}"
        binding.tvDescripcion.text = "Descripción: ${producto.descripcion}"

        binding.btnEditar.setOnClickListener {
            editar(producto)
        }

        binding.btnEliminar.setOnClickListener {
            eliminar(producto)
        }
    }

    override fun getItemCount(): Int {
        return productos.size
    }
}
