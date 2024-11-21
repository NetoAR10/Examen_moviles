package com.example.kotlin.examen.framework.adapter.viewholder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlin.examen_moviles.data.network.model.Historical
import com.example.kotlin.examen_moviles.databinding.ItemHistoricalBinding

class HistoricalViewHolder(
    private val binding: ItemHistoricalBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        historical: Historical,
        context: Context,) {// Configura el nombre de la postura en el TextView
        binding.descripcionTextView.text = historical.description
    }
}