package com.example.coursework.fragments.Outfits.update.more

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.R
import com.example.coursework.fragments.Clothes.list.ListAdapter
import com.example.coursework.fragments.Clothes.list.ListFragmentDirections
import com.example.coursework.viewModel.ClothesViewModel
import com.example.coursework.viewModel.SortType
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MoreAboutOutfitFragment : Fragment() {

    private lateinit var mClothesViewModel: ClothesViewModel
    private lateinit var adapter: MoreAboutOutfitAdapter
    private var outfitId: Int = 0 // Поле класса для хранения outfitId

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получаем аргументы и извлекаем outfitId
        //val args = MoreAboutOutfitFragmentArgs.fromBundle(requireArguments())
        //outfitId = args.outfit.id

        //RecyclerView
        adapter = MoreAboutOutfitAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //ViewModel
        mClothesViewModel = ViewModelProvider(requireActivity()).get(ClothesViewModel::class.java)

//        var backButton = view.findViewById<FloatingActionButton>(R.id.more_back_button)
//        backButton.setOnClickListener{
//            findNavController().navigate(R.id.action_moreAboutOutfitFragment_to_listFragment)
//        }

        // Обновляем адаптер и текст в случае изменения данных
        updateAdapter()
    }

    private fun updateAdapter() {
        // Получаем список одежды для заданного комплекта
        mClothesViewModel.getClothingItemsForOutfit(outfitId).observe(viewLifecycleOwner, Observer { clothingItems ->
            adapter.setData(clothingItems, outfitId)
        })
    }
}

