package com.example.coursework.fragments.Clothes.add_to_outfit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.R
import com.example.coursework.fragments.Outfits.list.OutfitListAdapter
import com.example.coursework.model.ClothingItem
import com.example.coursework.viewModel.ClothesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddToOutfitFragment : Fragment(){

    private lateinit var mClothesViewModel: ClothesViewModel
    private lateinit var adapter: AddToOutfitAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_to_outfit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewModel
        mClothesViewModel = ViewModelProvider(requireActivity()).get(ClothesViewModel::class.java)

        // RecyclerView
        adapter = AddToOutfitAdapter(mClothesViewModel)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //Получаем выбранные Clothing Items
        val selectedClothingItems = arguments?.getParcelableArray("selected_clothing_items") as? Array<ClothingItem>
        // Устанавливаем выбранные элементы одежды в адаптер
        adapter.setSelectedClothingItems(selectedClothingItems.orEmpty().toList())

        // Обновляем адаптер в случае изменения данных
        updateAdapter()

        val backButton = view.findViewById<FloatingActionButton>(R.id.add_to_outfit_back_button)
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun updateAdapter() {
        mClothesViewModel.readAllOutfits.observe(viewLifecycleOwner, Observer { outfits ->
            adapter.setData(outfits)
        })
    }
}