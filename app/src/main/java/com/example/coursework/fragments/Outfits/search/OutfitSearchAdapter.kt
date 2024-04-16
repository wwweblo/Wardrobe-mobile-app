package com.example.coursework.fragments.Outfits.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.R
import com.example.coursework.fragments.Clothes.search.SearchFragmentDirections
import com.example.coursework.model.Outfit
import com.example.coursework.viewModel.ClothesViewModel

class OutfitSearchAdapter(private val viewModel: ClothesViewModel) : RecyclerView.Adapter<OutfitSearchAdapter.MyViewHolder>() {

    private var outfitList = emptyList<Outfit>()

    private val titleLengthLimit = 35
    private val descriptionLengthLimit = 50

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val checkboxSelected: CheckBox = itemView.findViewById(R.id.custom_row_select)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row_list_without_checkbox, parent, false))
    }

    override fun getItemCount(): Int {
        return outfitList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = outfitList[position]

        holder.itemView.apply {
            //Title
            val titleTextView = findViewById<TextView>(R.id.list_adapter_title)
            textViewLimit(titleTextView, currentItem.title, titleLengthLimit)

            //Type
            findViewById<TextView>(R.id.list_adapter_style).text = currentItem.style

            //Season
            findViewById<TextView>(R.id.list_adapter_season).text = currentItem.season

            //Description
            val descriptionTextView = findViewById<TextView>(R.id.list_adapter_description)
            textViewLimit(descriptionTextView, currentItem.description, descriptionLengthLimit)

            //Передача элемента на окно обновления
            findViewById<ConstraintLayout>(R.id.rowLayout).setOnClickListener {
//                val action = SearchFragmentDirections.actionSearchFragmentToUpdateFragment(currentItem)
//                findNavController().navigate(action)
                val action = OutfitSearchFragmentDirections.actionOutfitSearchFragmentToOutfitUpdateFragment(currentItem)
                findNavController().navigate(action)
            }

        }
    }

    fun setData(outfits: List<Outfit>) {
        outfitList = outfits
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