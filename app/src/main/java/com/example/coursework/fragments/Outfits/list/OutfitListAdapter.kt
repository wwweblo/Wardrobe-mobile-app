package com.example.coursework.fragments.Outfits.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.R
import com.example.coursework.fragments.Clothes.list.ListFragmentDirections
import com.example.coursework.model.ClothingItem
import com.example.coursework.model.Outfit

class OutfitListAdapter:RecyclerView.Adapter<OutfitListAdapter.MyViewHolder>() {

    private var OutfitList = emptyList<Outfit>()

    private var titleLengthLimit = 35
    private var descriptionLengthLimit = 50
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row_list, parent, false))
    }

    override fun getItemCount(): Int {
        //Возвращает колличество элементов
        return OutfitList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = OutfitList[position]

        //Title
        val titleTextView = holder.itemView.findViewById<TextView>(R.id.list_adapter_title)
        textViewLimit(titleTextView, currentItem.title, titleLengthLimit)

        //Type
        holder.itemView.findViewById<TextView>(R.id.list_adapter_type).text = currentItem.style

        //Season
        holder.itemView.findViewById<TextView>(R.id.list_adapter_season).text = currentItem.season

        //Description
        val descriptionTextView = holder.itemView.findViewById<TextView>(R.id.list_adapter_description)
        textViewLimit(descriptionTextView, currentItem.description, descriptionLengthLimit)

        //Передача эелемента на окно обновления
//        holder.itemView.findViewById<ConstraintLayout>(R.id.rowLayout).setOnClickListener{
//            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
//            holder.itemView.findNavController().navigate(action)
//        }
    }

    fun setData(outfit: List<Outfit>) {
        this.OutfitList = outfit
        updateItems(outfit)
    }

    fun updateItems(outfit: List<Outfit>) {
        this.OutfitList = outfit
        //notifyDataSetChanged()
    }

    private fun textViewLimit(tv: TextView, text: String, limit: Int){
        if (text.length > limit) {
            tv.text = text.substring(0, titleLengthLimit) + "..."
        } else {
            tv.text = text
        }
    }
}