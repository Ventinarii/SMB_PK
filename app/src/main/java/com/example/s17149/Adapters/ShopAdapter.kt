package com.example.s17149.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.s17149.DataBase.Shop
import com.example.s17149.DataBase.ShopViewModel
import com.example.s17149.databinding.DataRowBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ShopAdapter(private val shopViewModel: ShopViewModel, private val shopEditInterface: ShopEditInterface): RecyclerView.Adapter<ShopAdapter.ProductViewHolder>() {

    private var products = emptyList<Shop>()

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
        val latitudeT = product.latitude.toString();
        val longtitudeT = product.longtitude.toString();

        holder.biding.namecheckBox.text = name;
        holder.biding.latitudeTextView.text = latitudeT;
        holder.biding.longtitudeTextView.text = longtitudeT;
        holder.biding.namecheckBox.isChecked = product.favorite;
        holder.id = product.id;

        holder.biding.deletebutton.setOnClickListener{
            CoroutineScope(IO).launch { shopViewModel.delete(product,holder.biding.root.context) }
            Toast.makeText(holder.biding.root.context,"deleted: "+name,Toast.LENGTH_SHORT).show();
        }
        holder.biding.editbutton.setOnClickListener{
            shopEditInterface.editProductOver(product);
        }
        holder.biding.namecheckBox.setOnCheckedChangeListener { buttonView, isChecked -> kotlin.run {
            product.favorite = isChecked;
            CoroutineScope(IO).launch { shopViewModel.update(null,product,holder.biding.root.context) }
            Toast.makeText(holder.biding.root.context,"updated: "+name,Toast.LENGTH_SHORT).show();
        }
        }
    }

    override fun getItemCount(): Int = products.size;

    fun setProducts(shops: List<Shop> ){
        this.products = shops;
        notifyDataSetChanged();
    }
}