package com.example.coursework.fragments.Clothes.list

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
import com.example.coursework.viewModel.ClothesViewModel
import com.example.coursework.viewModel.SortType
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListFragment : Fragment() {

    private lateinit var mClothesViewModel: ClothesViewModel
    private lateinit var adapter: ListAdapter

    private lateinit var canselButton: Button
    private lateinit var addToOutFitButton: ImageButton
    private lateinit var addButton: FloatingActionButton
    private  lateinit var searchButton: ImageButton
    private  lateinit var sortButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ViewModel
        mClothesViewModel = ViewModelProvider(requireActivity()).get(ClothesViewModel::class.java)

        //RecyclerView
        adapter = ListAdapter(mClothesViewModel)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //Инициализация кнопок
        addButton = view.findViewById(R.id.list_add_button)
        searchButton = view.findViewById(R.id.list_search_button)
        sortButton = view.findViewById(R.id.list_sort_button)
        canselButton = view.findViewById(R.id.list_cansel_button)
        addToOutFitButton = view.findViewById(R.id.list_more_button)

        //Добавление
        addButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        //Поиск
        searchButton.setOnClickListener{
            findNavController().navigate(R.id.action_listFragment_to_searchFragment)
        }

        //Сортировка
        sortButton.setOnClickListener {
            mClothesViewModel.changeClothesSortType()
            val message = when (mClothesViewModel.currentClothesSortType) {
                SortType.BY_TITLE -> getString(R.string.sort_type_title)
                SortType.BY_DATE_UPDATED -> getString(R.string.sort_type_last_update)
            }
            showToast(message)

            updateAdapter()
        }

        //Отмена выбора
        canselButton.setOnClickListener {
            adapter.resetSelection()
        }

        //Добавление в образ
        addToOutFitButton.setOnClickListener{
            // Получаем список всех элементов из ViewModel
            val allClothingItems = mClothesViewModel.readAllClothes.value?: emptyList()

            // Отфильтровываем список, оставляя только выбранные элементы
            val selectedClothingItems = allClothingItems.filter { it.isSelected?: false }

            // Если массив выбранных элементов не пустой, формируем и передаем его
            if (selectedClothingItems.isNotEmpty()) {
                // Формируем массив из выбранных элементов
                val selectedItemsArray = selectedClothingItems.toTypedArray()

                // Создаем Bundle для передачи данных в следующий фрагмент
                val bundle = Bundle().apply {
                    putParcelableArray("selectedItems", selectedItemsArray)
                }


                // Навигация на addToOutfitFragment с передачей Bundle
                findNavController().navigate(R.id.action_listFragment_to_addToOutfitFragment, bundle)
            } else {
                // Если массив пуст, отображаем сообщение пользователю
                showToast(getString(R.string.choose_clothes_to_add))
            }
        }

        // Обновляем адаптер
        updateAdapter()
    }



    private fun updateAdapter(){
        mClothesViewModel.readAllClothes.observe(viewLifecycleOwner, Observer { clothingItems ->
            adapter.setData(clothingItems)
            updateListTitle() // Обновляем текст после обновления данных

            //Так как выполняется запрос в бд запускаем в дургом потоке
            CoroutineScope(Dispatchers.IO).launch {
                hideSelectionButtons()  //Скрывает кнопки: отмены выделения и добавления в outfit если ни один предмет не выбран
            }
        })

    }

    private suspend fun hideSelectionButtons() {
//        val anyItemSelected = mClothesViewModel.isAnyItemSelected()
//        if (!anyItemSelected) {
//            // Ни у одного элемента не установлен isSelected в true
//            canselButton?.visibility = View.GONE
//            addToOutFitButton?.visibility = View.GONE
//
//            searchButton?.visibility = View.VISIBLE
//            sortButton?.visibility = View.VISIBLE
//        }
//        else{
//            canselButton?.visibility = View.VISIBLE
//            addToOutFitButton?.visibility = View.VISIBLE
//
//            searchButton?.visibility = View.GONE
//            sortButton?.visibility = View.GONE
//        }
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
