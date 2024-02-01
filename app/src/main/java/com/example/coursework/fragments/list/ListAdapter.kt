package com.example.coursework.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.R
import com.example.coursework.model.ClothingItem

class ListAdapter:RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var clothingItemList = emptyList<ClothingItem>()

    private var titleLenthLimit = 23
    private var descriptionLenghLimit = 30
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false))
    }

    override fun getItemCount(): Int {
        //Возвращает колличество элементов
        return clothingItemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = clothingItemList[position]

        //Title
        val titleTextView = holder.itemView.findViewById<TextView>(R.id.list_adapter_title)
        if (currentItem.title.length > titleLenthLimit) {
            titleTextView.text = currentItem.title.substring(0, titleLenthLimit) + "..."
        } else {
            titleTextView.text = currentItem.title
        }

        //Season
        holder.itemView.findViewById<TextView>(R.id.list_adapter_season).text = currentItem.season

        //Description
        val descriptionTextView = holder.itemView.findViewById<TextView>(R.id.list_adapter_description)
        if (currentItem.description.length > descriptionLenghLimit) {
            descriptionTextView.text = currentItem.description.substring(0, descriptionLenghLimit) + "..."
        } else {
            descriptionTextView.text = currentItem.description
        }

        //Передача эелемента на окно обновления
        holder.itemView.findViewById<ConstraintLayout>(R.id.rowLayout).setOnClickListener{
            val action = ListFragmentDirections.actionListFragmentToUpdateFregment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
    }

    fun setData(clothingItem: List<ClothingItem>){
        this.clothingItemList = clothingItem
        notifyDataSetChanged()
    }


}