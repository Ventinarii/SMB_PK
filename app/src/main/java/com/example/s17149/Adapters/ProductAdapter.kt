package com.example.s17149.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.s17149.DataBase.Product
import com.example.s17149.DataBase.ProductViewModel
import com.example.s17149.Logic.AppLogic
import com.example.s17149.databinding.DataRowBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.lang.Appendable
import java.security.acl.Owner

class ProductAdapter(private val productViewModel: ProductViewModel, private val productEditInterface: ProductEditInterface): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    private var products = emptyList<Product>()
    private lateinit var context: Context;

    class ProductViewHolder(val biding: DataRowBinding):RecyclerView.ViewHolder(biding.root){
        public var id: Int = 0;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context);
        context = parent.context;
        return ProductViewHolder(biding = DataRowBinding.inflate(inflater));
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position];
        val name = product.Name;
        val qty = product.Qty.toString();
        val price = product.Price.toString() + "PLN/qty"

        holder.biding.namecheckBox.text = name;
        holder.biding.qtytextView.text = qty;
        holder.biding.costtextView.text = price;
        holder.biding.namecheckBox.isChecked = product.Click;

        holder.biding.PublicTextView.setText(if (product.Public) "Public" else "Private");
        holder.biding.UidOfOwnerTextView.setText("Owner has ID: ${product.UserId}");

        if(!AppLogic.fUser.uid.equals(product.UserId)){
            holder.biding.deletebutton.isEnabled=false;
            holder.biding.editbutton.isEnabled=false;
            if(!product.Public)
                Toast.makeText(context,"This item SHOULD NOT be visible here: ${product.Id}, ${product.UserId}, ${product.Public}",Toast.LENGTH_LONG).show();
        }

        holder.id = product.Id;

        holder.biding.deletebutton.setOnClickListener{
            CoroutineScope(IO).launch { productViewModel.delete(product) }
            Toast.makeText(holder.biding.root.context,"deleted: "+name,Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        }
        holder.biding.editbutton.setOnClickListener{
            productEditInterface.editProductOver(product);
        }
        holder.biding.namecheckBox.setOnCheckedChangeListener { buttonView, isChecked -> kotlin.run {
            product.Click = isChecked;
            CoroutineScope(IO).launch { productViewModel.update(product) }
            Toast.makeText(holder.biding.root.context,"updated: "+name,Toast.LENGTH_SHORT).show();
        }
        }
    }

    override fun getItemCount(): Int = products.size;

    fun setProducts(products: List<Product> ){
        this.products = products;
        notifyDataSetChanged();
    }
}