package com.example.s17149.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.s17149.DataBase.Product
import com.example.s17149.DataBase.ProductViewModel
import com.example.s17149.databinding.DataRowBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ProductAdapter(private val productViewModel: ProductViewModel, private val productEditInterface: ProductEditInterface): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var products = emptyList<Product>()

    class ProductViewHolder(val biding: DataRowBinding):RecyclerView.ViewHolder(biding.root){
        public var id: Long = 0;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context);
        return ProductViewHolder(biding = DataRowBinding.inflate(inflater));
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position];
        val name = product.name;
        val qty = product.qty.toString();
        val price = product.price.toString() + "PLN/qty"

        holder.biding.namecheckBox.text = name;
        holder.biding.qtytextView.text = qty;
        holder.biding.costtextView.text = price;
        holder.id = product.id;

        holder.biding.deletebutton.setOnClickListener{
            CoroutineScope(IO).launch { productViewModel.delete(product) }
            Toast.makeText(holder.biding.root.context,"deleted: "+name,Toast.LENGTH_SHORT).show();
        }
        holder.biding.editbutton.setOnClickListener{
            productEditInterface.editProductOver(product);
        }
    }

    override fun getItemCount(): Int = products.size;
}