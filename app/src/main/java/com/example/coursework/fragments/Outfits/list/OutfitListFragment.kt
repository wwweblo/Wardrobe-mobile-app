package com.example.coursework.fragments.Outfits.list

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

class OutfitListFragment : Fragment() {

    private lateinit var mClothesViewModel: ClothesViewModel
    private lateinit var adapter: OutfitListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_outfit_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView
        adapter = OutfitListAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ViewModel
        mClothesViewModel = ViewModelProvider(requireActivity()).get(ClothesViewModel::class.java)

        // Обновляем адаптер и текст в случае изменения данных
        updateAdapter()

        val sortButton = view.findViewById<ImageButton>(R.id.outfit_list_sort_button)
        //Сортировка
        sortButton.setOnClickListener {
            mClothesViewModel.changeOutfitSortType()
            val message = when (mClothesViewModel.currentOutfitsSortType) {
                SortType.BY_TITLE -> getString(R.string.sort_type_title)
                SortType.BY_DATE_UPDATED -> getString(R.string.sort_type_last_update)
            }
            showToast(message)

            updateAdapter()
        }

        // Кнопка добавления нового комплекта
        val addButton = view.findViewById<FloatingActionButton>(R.id.outfit_list_add_button)
        addButton.setOnClickListener {
            findNavController().navigate(R.id.action_outfitListFragment_to_outfitAddFragment)
        }
    }

    private fun updateAdapter(){
        mClothesViewModel.readAllOutfits.observe(viewLifecycleOwner, Observer { outfits ->
            adapter.setData(outfits)
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
        if (adapter.itemCount == 0) {
            listTitle?.apply {
                text = getString(R.string.empty_list_fragment_title)
                textSize = 24f
                gravity = Gravity.CENTER
            }
        } else {
            listTitle?.apply {
                text = getString(R.string.clothes)
                textSize = 34f
                gravity = Gravity.CENTER
            }
        }
    }
}
