package com.example.coursework.fragments.Outfits.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coursework.R
import com.example.coursework.fragments.Clothes.search.SearchListAdapter
import com.example.coursework.viewModel.ClothesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


@Suppress("DEPRECATION")
class OutfitSearchFragment : Fragment() {

    private lateinit var mClothesViewModel: ClothesViewModel
    private lateinit var adapter: OutfitSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        //ViewModel
        mClothesViewModel = ViewModelProvider(this).get(ClothesViewModel::class.java)

        //RecyclerView
        adapter = OutfitSearchAdapter(mClothesViewModel)
        val recyclerView = view.findViewById<RecyclerView>(R.id.search_recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Кнопка назад
        val backButton = view.findViewById<FloatingActionButton>(R.id.search_back_button)
        backButton.setOnClickListener{
            findNavController().popBackStack()
        }

        showKeyboard()
        // Установка фокуса на EditText для начала ввода поискового запроса
        val searchInput = view.findViewById<EditText>(R.id.search_editText)
        searchInput.requestFocus()

        // Обновление списка при изменении текста в EditText
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Ничего не делаем
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Ничего не делаем
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim()
                updateAdapter(searchText)
            }
        })

        return view
    }

    private fun updateAdapter(searchText: String) {
        mClothesViewModel.readAllOutfits.observe(viewLifecycleOwner, Observer { clothingItems ->
            val filteredList = clothingItems.filter { item ->
                item.title.contains(searchText, ignoreCase = true) ||
                        item.season.contains(searchText, ignoreCase = true) ||
                        item.description.contains(searchText, ignoreCase = true) //||
                //item.style.contains(searchText, ignoreCase = true)
            }
            adapter.setData(filteredList)
        })
    }

    private fun showKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}