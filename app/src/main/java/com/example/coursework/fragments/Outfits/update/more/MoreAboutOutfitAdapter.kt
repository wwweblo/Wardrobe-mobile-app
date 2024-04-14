package com.example.coursework.fragments.Outfits.update.more

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.R
import com.example.coursework.fragments.Clothes.list.ListFragmentDirections
import com.example.coursework.model.ClothingItem
import com.example.coursework.viewModel.ClothesViewModel

class MoreAboutOutfitAdapter(private val activity: FragmentActivity) : RecyclerView.Adapter<MoreAboutOutfitAdapter.MyViewHolder>() {

    private var mClothesViewModel: ClothesViewModel = ViewModelProvider(activity).get(ClothesViewModel::class.java)
    private val clothingItems = mutableListOf<ClothingItem>()
    private var outfitId: Int = -1

    companion object {
        private const val TITLE_LENGTH_LIMIT = 35
        private const val DESCRIPTION_LENGTH_LIMIT = 50
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_row_list_without_checkbox, parent, false)
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
            findViewById<TextView>(R.id.list_adapter_style).text = currentItem.type

            // Season
            findViewById<TextView>(R.id.list_adapter_season).text = currentItem.season

            // Description
            val descriptionTextView = findViewById<TextView>(R.id.list_adapter_description)
            textViewLimit(descriptionTextView, currentItem.description, DESCRIPTION_LENGTH_LIMIT)

            // Передача элемента на окно обновления
            findViewById<ConstraintLayout>(R.id.rowLayout).setOnClickListener {
                val action = MoreAboutOutfitFragmentDirections.actionMoreAboutOutfitFragmentToUpdateFragment(currentItem)
                findNavController().navigate(action)
            }
        }
    }

    fun setData(clothingItems: List<ClothingItem>, outfitId: Int) {
        this.outfitId = outfitId
        mClothesViewModel.getClothingItemsForOutfit(outfitId).observeForever { clothingItemsForOutfit ->
            this.clothingItems.clear()
            this.clothingItems.addAll(clothingItemsForOutfit)
            notifyDataSetChanged()
        }
    }

    private fun textViewLimit(tv: TextView, text: String, limit: Int) {
        if (text.length > limit) {
            tv.text = text.substring(0, limit) + "..."
        } else {
            tv.text = text
        }
    }

    fun submitList(clothingItems: List<ClothingItem>, outfitId: Int) {
        this.outfitId = outfitId
        this.clothingItems.clear()
        this.clothingItems.addAll(clothingItems)
        notifyDataSetChanged()
    }

}
