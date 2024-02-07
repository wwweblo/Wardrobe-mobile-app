package com.example.coursework.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.R
import com.example.coursework.viewModel.ClothesViewModel
import com.example.coursework.viewModel.SortType
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListFragment : Fragment() {

    private lateinit var mClothesViewModel: ClothesViewModel
    private lateinit var adapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        //RecyclerView
        adapter = ListAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //ViewModel
        mClothesViewModel = ViewModelProvider(this).get(ClothesViewModel::class.java)

        //Обновляем адаптер и текст в случае изменения данных
        updateAdapter()

        //Кнопка добавления новой одежды
        val addButton = view.findViewById<FloatingActionButton>(R.id.list_add_button)
        addButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        //Кнопка Сортировки
        val sortButton = view.findViewById<ImageButton>(R.id.list_sort_button)
        sortButton.setOnClickListener {
            mClothesViewModel.changeSortType()
            val message = when (mClothesViewModel.currentSortType) {
                SortType.BY_TITLE -> getString(R.string.sort_type_title)
                SortType.BY_DATE_UPDATED -> getString(R.string.sort_type_last_update)
            }
            showToast(message)

            updateAdapter()
        }

        return view
    }

    private fun updateAdapter(){
        mClothesViewModel.readAllData.observe(viewLifecycleOwner, Observer { clothingItem ->
            adapter.setData(clothingItem)
            updateListTitle() // Обновляем текст после обновления данных
        })
    }

    private var toast: Toast? = null
    private fun showToast(message: String) {
        // Отменяем текущее всплывающее сообщение, если оно существует
        toast?.cancel()
        // Создаем и показываем новое всплывающее сообщение
        toast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    private fun updateListTitle() {
        val listTitle = view?.findViewById<TextView>(R.id.list_title)
        //showToast(adapter.itemCount.toString())
        if (adapter.itemCount == 0) {
            listTitle?.apply {
                text = getString(R.string.empty_list_fragment_title)
                textSize = 24f // Установка размера текста в 26dp
                gravity = Gravity.CENTER // Центрирование текста по центру
            }
        } else {
            listTitle?.apply {
                text = getString(R.string.list_fragment_title)
                textSize = 34f
                gravity = Gravity.CENTER // Вернуть текст в центр по умолчанию
            }
        }
    }



}

