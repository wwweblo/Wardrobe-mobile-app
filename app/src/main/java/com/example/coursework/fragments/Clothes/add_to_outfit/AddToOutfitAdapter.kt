package com.example.coursework.fragments.Clothes.add_to_outfit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.navigation.navArgument
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.R
import com.example.coursework.fragments.Clothes.list.ListFragmentDirections
import com.example.coursework.model.ClothingItem
import com.example.coursework.model.ClothingItemOutfitCrossRef
import com.example.coursework.model.Outfit
import com.example.coursework.viewModel.ClothesViewModel


class AddToOutfitAdapter(private val viewModel: ClothesViewModel) : RecyclerView.Adapter<AddToOutfitAdapter.MyViewHolder>() {

    private var outfits = emptyList<Outfit>()
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
                val action =
                    AddToOutfitFragmentDirections.actionAddToOutfitFragmentToMoreAboutOutfitFragment(currentItem)
                findNavController().navigate(action)
            }

        }
    }

    fun setData(outfits: List<Outfit>) {
        this.outfits = outfits
        notifyDataSetChanged()
    }

    fun insertSelectedClothingItemsForOutfit(outfitId: Int, selectedClothingItems: List<ClothingItem>) {

    }

}
