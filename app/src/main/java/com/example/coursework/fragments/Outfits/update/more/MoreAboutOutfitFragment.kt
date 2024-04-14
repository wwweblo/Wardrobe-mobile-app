package com.example.coursework.fragments.Outfits.update.more

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.R
import com.example.coursework.fragments.Outfits.list.OutfitListAdapter
import com.example.coursework.model.ClothingItem
import com.example.coursework.model.Outfit
import com.example.coursework.viewModel.ClothesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MoreAboutOutfitFragment : Fragment() {

    private lateinit var mClothesViewModel: ClothesViewModel
    private val args: MoreAboutOutfitFragmentArgs by navArgs()
    private lateinit var adapter: MoreAboutOutfitAdapter
    private lateinit var title: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more_about_outfit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView
        adapter = MoreAboutOutfitAdapter(requireActivity())
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ViewModel
        mClothesViewModel = ViewModelProvider(requireActivity()).get(ClothesViewModel::class.java)
        val currentOutfit = args.currentOutfit

        title = view.findViewById(R.id.more_title)

        // Обновляем адаптер и текст в случае изменения данных
        updateAdapter(currentOutfit)
    }

    private fun updateAdapter(currentOutfit : Outfit?) {
        if (currentOutfit != null){
            title.text = currentOutfit.title
            mClothesViewModel.getClothingItemsForOutfit(currentOutfit.id).observe(viewLifecycleOwner, Observer { clothes ->
                adapter.setData(clothes, currentOutfit.id)
            })
        } else {
            title.text = getString(R.string.null_outfit)
        }
    }


}