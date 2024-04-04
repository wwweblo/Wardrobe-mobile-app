package com.example.coursework.fragments.Clothes.add_to_outfit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.R
import com.example.coursework.model.Outfit


class AddToOutfitAdapter : RecyclerView.Adapter<AddToOutfitAdapter.MyViewHolder>() {

    private var outfits = emptyList<Outfit>()
    private var itemClickListener: OnItemClickListener? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_row_list_without_checkbox, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return outfits.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = outfits[position]

        holder.itemView.apply {
            // Title
            findViewById<TextView>(R.id.list_adapter_title).text = currentItem.title

            // style
            findViewById<TextView>(R.id.list_adapter_style).text = currentItem.style

            // Season
            findViewById<TextView>(R.id.list_adapter_season).text = currentItem.season

            // Description
            findViewById<TextView>(R.id.list_adapter_description).text = currentItem.description

            // Передача элемента на окно обновления
            findViewById<ConstraintLayout>(R.id.rowLayout).setOnClickListener {
                itemClickListener?.onItemClick(currentItem.id)
            }
        }
    }

    fun setData(outfits: List<Outfit>) {
        this.outfits = outfits
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(outfitId: Int)
    }
}
