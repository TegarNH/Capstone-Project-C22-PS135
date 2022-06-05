package com.teamc22ps135.healthlens.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.teamc22ps135.healthlens.R
import com.teamc22ps135.healthlens.data.remote.response.ProductList
import com.teamc22ps135.healthlens.databinding.ItemProductRecommendationBinding

class ProductRecomAdapter : RecyclerView.Adapter<ProductRecomAdapter.ViewHolder>() {

    private val listProductRecom = ArrayList<ProductList>()

    @SuppressLint("NotifyDataSetChanged")
    fun setDataProductRecom(dataProductRecom: List<ProductList>) {
        listProductRecom.clear()
        listProductRecom.addAll(dataProductRecom)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemProductRecommendationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(listProductRecom[position])
    }

    override fun getItemCount(): Int = listProductRecom.size

    class ViewHolder(private val view: ItemProductRecommendationBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun binding(productList: ProductList) {
            with(view) {
                nameProduct.text = productList.name

                Glide.with(itemView.context)
                    .load(productList.photo)
                    .placeholder(R.drawable.ic_baseline_photo_24)
                    .error(R.drawable.ic_baseline_photo_24)
                    .into(imageProduct)

                btnBuyHere.setOnClickListener {
                    val uri = Uri.parse(productList.linkProduct)
                    val visitWebGithub = Intent(Intent.ACTION_VIEW, uri)
                    itemView.context.startActivity(visitWebGithub)
                }
            }
        }
    }
}