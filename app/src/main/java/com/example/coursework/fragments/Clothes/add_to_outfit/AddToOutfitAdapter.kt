package com.example.coursework.fragments.Clothes.add_to_outfit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.R
import com.example.coursework.fragments.Outfits.list.OutfitListFragmentDirections
import com.example.coursework.model.Outfit

class AddToOutfitAdapter : RecyclerView.Adapter<AddToOutfitAdapter.MyViewHolder>() {

    private var outfitList = emptyList<Outfit>()
    private var onItemClickListener: ((Outfit) -> Unit)? = null

    private val titleLengthLimit = 35
    private val descriptionLengthLimit = 50
    fun setOnItemClickListener(listener: (Outfit) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(outfits: List<Outfit>) {
        outfitList = outfits
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_row_list_without_checkbox,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return outfitList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = outfitList[position]

        holder.itemView.apply {
            // Title
            val titleTextView = findViewById<TextView>(R.id.list_adapter_title)
            textViewLimit(titleTextView, currentItem.title, titleLengthLimit)

            // Type
            findViewById<TextView>(R.id.list_adapter_type).text = currentItem.style

            // Season
            findViewById<TextView>(R.id.list_adapter_season).text = currentItem.season

            // Description
            val descriptionTextView = findViewById<TextView>(R.id.list_adapter_description)
            textViewLimit(descriptionTextView, currentItem.description, descriptionLengthLimit)

            // Установка слушателя нажатий на элемент
            setOnClickListener {
                onItemClickListener?.invoke(currentItem)
            }
        }
    }

    private fun textViewLimit(tv: TextView, text: String, limit: Int) {
        tv.text = if (text.length > limit) {
            text.substring(0, limit) + "..."
        } else {
            text
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
}
