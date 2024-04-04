package com.example.coursework.fragments.Clothes.add_to_outfit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.R
import com.example.coursework.fragments.Outfits.list.OutfitListAdapter
import com.example.coursework.viewModel.ClothesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddToOutfitFragment : Fragment(), AddToOutfitAdapter.OnItemClickListener {

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

        // RecyclerView
        adapter = AddToOutfitAdapter()
        adapter.setOnItemClickListener(this)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ViewModel
        mClothesViewModel = ViewModelProvider(requireActivity()).get(ClothesViewModel::class.java)

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

    override fun onItemClick(outfitId: Int) {
        // Выполнение действий при нажатии на элемент RecyclerView
        // Например, передача outfitId на следующий фрагмент
        val action = AddToOutfitFragmentDirections.actionAddToOutfitFragmentToMoreAboutOutfitFragment(outfitId)
        findNavController().navigate(R.id.action_addToOutfitFragment_to_moreAboutOutfitFragment)
    }
}