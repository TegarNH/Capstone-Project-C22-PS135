package com.teamc22ps135.healthlens.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamc22ps135.healthlens.data.remote.response.Story
import com.teamc22ps135.healthlens.databinding.ItemRecommendationBinding

class RecommendationAdapter : RecyclerView.Adapter<RecommendationAdapter.ViewHolder>() {

    private val listRecommendation = ArrayList<Story>()

    fun setDataRecommendation(dataRecommendation: List<Story>) {
        listRecommendation.clear()
        listRecommendation.addAll(dataRecommendation)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(listRecommendation[position])
    }

    override fun getItemCount(): Int = listRecommendation.size

    class ViewHolder(private val view: ItemRecommendationBinding) :
        RecyclerView.ViewHolder(view.root) {

        @SuppressLint("SetTextI18n")
        fun binding(user: Story) {
//            val content = "<ol><li>${user.description}</li></ol>"
//            view.pointRecommendation.text = Html.fromHtml(content)
            view.pointRecommendation.text =
                "${RecommendationAdapter().listRecommendation.size}  ${user.description}"
        }
    }
}