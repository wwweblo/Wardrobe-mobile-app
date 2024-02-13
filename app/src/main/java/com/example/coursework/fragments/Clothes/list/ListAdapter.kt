package com.example.coursework.fragments.Clothes.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.R
import com.example.coursework.model.ClothingItem

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private val clothingItems = mutableListOf<ClothingItem>()

    companion object {
        private const val TITLE_LENGTH_LIMIT = 35
        private const val DESCRIPTION_LENGTH_LIMIT = 50
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_row_list, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return clothingItems.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = clothingItems[position]

        holder.itemView.apply {
            // Title
            val titleTextView = findViewById<TextView>(R.id.list_adapter_title)
            textViewLimit(titleTextView, currentItem.title, TITLE_LENGTH_LIMIT)

            // Type
            findViewById<TextView>(R.id.list_adapter_type).text = currentItem.type

            // Season
            findViewById<TextView>(R.id.list_adapter_season).text = currentItem.season

            // Description
            val descriptionTextView = findViewById<TextView>(R.id.list_adapter_description)
            textViewLimit(descriptionTextView, currentItem.description, DESCRIPTION_LENGTH_LIMIT)

            // Передача элемента на окно обновления
            findViewById<ConstraintLayout>(R.id.rowLayout).setOnClickListener {
                val action =
                    ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
                findNavController().navigate(action)
            }
        }
    }

    fun setData(clothingItems: List<ClothingItem>) {
        this.clothingItems.clear()
        this.clothingItems.addAll(clothingItems)
        notifyDataSetChanged()
    }

    private fun textViewLimit(tv: TextView, text: String, limit: Int) {
        if (text.length > limit) {
            tv.text = text.substring(0, limit) + "..."
        } else {
            tv.text = text
        }
    }

}