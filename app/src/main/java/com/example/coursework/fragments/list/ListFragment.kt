package com.example.coursework.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.R
import com.example.coursework.viewModel.ClothesViewModel
import com.example.coursework.viewModel.SortOrder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListFragment : Fragment() {

    private lateinit var mClothesViewModel: ClothesViewModel
    private var sortOrder = SortOrder.ASCENDING
    private lateinit var adapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        // RecyclerView
        adapter = ListAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ViewModel
        mClothesViewModel = ViewModelProvider(this).get(ClothesViewModel::class.java)

        // Наблюдение за изменением данных в LiveData
        mClothesViewModel.readAllData.observe(viewLifecycleOwner, Observer { clothingItem ->
            // Устанавливаем данные в адаптер с учетом текущей сортировки
            adapter.setData(if (sortOrder == SortOrder.ASCENDING) clothingItem else clothingItem.reversed())
        })

        // Наблюдение за изменением сортированных данных в LiveData
        mClothesViewModel.readAllData.observe(viewLifecycleOwner, Observer { sortedItems ->
            // Устанавливаем данные в адаптер
            adapter.setData(sortedItems)
        })


        // Кнопка сортировки
        val sortButton = view.findViewById<ImageButton>(R.id.list_sort_button)
        sortButton.setOnClickListener {
            mClothesViewModel.toggleSortOrder()
        }

        // Кнопка добавления
        val addButton = view.findViewById<FloatingActionButton>(R.id.list_add_button)
        addButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        return view
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}