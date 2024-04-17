package com.example.coursework.fragments.Clothes.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.R
import com.example.coursework.model.ClothingItem
import com.example.coursework.viewModel.ClothesViewModel

class SearchListAdapter(private val viewModel: ClothesViewModel) : RecyclerView.Adapter<SearchListAdapter.MyViewHolder>() {

    private var clothingItemList = emptyList<ClothingItem>()

    private val titleLengthLimit = 35
    private val descriptionLengthLimit = 50

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkboxSelected: CheckBox = itemView.findViewById(R.id.custom_row_select)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row_list, parent, false))
    }

    override fun getItemCount(): Int {
        return clothingItemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = clothingItemList[position]

        holder.itemView.apply {
            //Title
            val titleTextView = findViewById<TextView>(R.id.list_adapter_title)
            textViewLimit(titleTextView, currentItem.title, titleLengthLimit)

            //Type
            findViewById<TextView>(R.id.list_adapter_style).text = currentItem.type

            //Season
            findViewById<TextView>(R.id.list_adapter_season).text = currentItem.season

            //Description
            val descriptionTextView = findViewById<TextView>(R.id.list_adapter_description)
            textViewLimit(descriptionTextView, currentItem.description, descriptionLengthLimit)

            //Передача элемента на окно обновления
            findViewById<ConstraintLayout>(R.id.rowLayout).setOnClickListener {
                val action = SearchFragmentDirections.actionSearchFragmentToUpdateFragment(currentItem)
                findNavController().navigate(action)
            }

            // Установка значения CheckBox
            holder.checkboxSelected.isChecked = currentItem.isSelected ?: false

            // Обработчик нажатия на CheckBox
            holder.checkboxSelected.setOnCheckedChangeListener { _, isChecked ->
                // Обновление значения в базе данных
                currentItem.isSelected = isChecked
                // Вызов метода toggleClothingItemSelection из ViewModel
                viewModel.toggleClothingItemSelection(currentItem.id)
                Log.d("ListAdapter", "currentItem = (title->${currentItem.title}, isSelected->${currentItem.isSelected})")
            }
        }
    }

    fun setData(clothingItem: List<ClothingItem>) {
        clothingItemList = clothingItem
        notifyDataSetChanged()
    }

    private fun textViewLimit(tv: TextView, text: String, limit: Int) {
        if (text.length > limit) {
            tv.text = text.substring(0, titleLengthLimit) + "..."
        } else {
            tv.text = text
        }
    }
}
