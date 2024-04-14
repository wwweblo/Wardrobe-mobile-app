package com.example.coursework.fragments.Clothes.add_to_outfit

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgument
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.R
import com.example.coursework.fragments.Clothes.list.ListAdapter
import com.example.coursework.fragments.Clothes.list.ListFragmentDirections
import com.example.coursework.fragments.Clothes.update.UpdateFragmentArgs
import com.example.coursework.model.ClothingItem
import com.example.coursework.model.ClothingItemOutfitCrossRef
import com.example.coursework.model.Outfit
import com.example.coursework.viewModel.ClothesViewModel


class AddToOutfitAdapter(private val viewModel: ClothesViewModel) : RecyclerView.Adapter<AddToOutfitAdapter.MyViewHolder>() {

    private var outfits = emptyList<Outfit>()
    private var selectedClothingItems: List<ClothingItem> = emptyList()

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

            // Передача элемента на окно More
            findViewById<ConstraintLayout>(R.id.rowLayout).setOnClickListener {
                //создаем связи между selectedClothingItems и нажатым элементом
                insertSelectedClothingItemsForOutfit(currentItem.id, selectedClothingItems)

                //Переводим пользователя
                val action = AddToOutfitFragmentDirections.actionAddToOutfitFragmentToMoreAboutOutfitFragment(currentItem)
                findNavController().navigate(action)
            }
        }
    }

    fun setData(outfits: List<Outfit>) {
        this.outfits = outfits
        notifyDataSetChanged()
    }
    //Метод для получения selectedClothingItems с фрагмента
    fun setSelectedClothingItems(selectedClothingItems: List<ClothingItem>) {
        this.selectedClothingItems = selectedClothingItems
        notifyDataSetChanged()
    }

    private fun insertSelectedClothingItemsForOutfit(outfitId: Int, selectedClothingItems: List<ClothingItem>) {
        //Для каждого элемента из selectedClothingItems создаем ClothingItemOutfitCrossRef
        val clothingItemOutfitCrossRefs = selectedClothingItems.map { clothingItem ->
            ClothingItemOutfitCrossRef(
                clothingItemId = clothingItem.id,
                outfitId = outfitId
            )
        }
        // Добавляем каждую связь ClothingItemOutfitCrossRef в базу данных через ViewModel
        clothingItemOutfitCrossRefs.forEach { clothingItemOutfitCrossRef ->
            Log.d("AddToOutfitAdapter", "new addClothingItemOutfitCrossRef (clothingItemId = ${clothingItemOutfitCrossRef.clothingItemId}, outfitId = ${clothingItemOutfitCrossRef.outfitId})")
            viewModel.addClothingItemOutfitCrossRef(clothingItemOutfitCrossRef)
        }
    }



}
