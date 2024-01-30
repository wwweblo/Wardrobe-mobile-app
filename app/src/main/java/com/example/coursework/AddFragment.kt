package com.example.coursework

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.coursework.data.ClothesViewModel
import com.example.coursework.data.ClothingItem

class AddFragment : Fragment() {

    private lateinit var mClothingItemView: ClothesViewModel
    private lateinit var imageButton: ImageButton
    private var imagePath: String? = null // Переменная для хранения пути к изображению

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        mClothingItemView = ViewModelProvider(this).get(ClothesViewModel::class.java)

        imageButton = view.findViewById(R.id.add_imageButton)
        imageButton.setOnClickListener {
            openGalleryForImage()
        }

        view.findViewById<Button>(R.id.add_add_button).setOnClickListener {
            insertDataToDatabase(view)
        }

        return view
    }

    private fun openGalleryForImage() {
        /*
        *   Создаем intent для открытия изображений на устройстве
        *   и запускаетм для получения результата
        */
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    /*
    *   Создается переменная resultLauncher, которая является экземпляром ActivityResultLauncher,
    *   созданного с использованием ActivityResultContracts.StartActivityForResult().
    *   Он обрабатывает результат запуска активности для результата.
    *
    *   Если результат положительный, то из результата intent берется изображение
    *   и извлекается его url. На imageButton ставится изображение по полученному url,
    *   а imagePath принимает значение url
    */
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let { selectedImage ->
                imageButton.setImageURI(selectedImage)
                imagePath = selectedImage.path
                //showToast("Путь к изображению: $imagePath")
            }
        }

    }

    private fun insertDataToDatabase(view: View) {
        //Получаем заполнение из editTextов
        val title = view.findViewById<EditText>(R.id.add_title_imput).text.toString()
        val season = view.findViewById<EditText>(R.id.add_season_imput).text.toString()
        val description = view.findViewById<EditText>(R.id.add_descriptlion_imput).text.toString()

        //Если title не null одбавляем clothingItem
        if (EmptyStringCheck(title)){
            val clothingItem = ClothingItem(
                id = 0,
                image = imagePath,
                title = title,
                season = season,
                description = description
            )
            mClothingItemView.addClothingItem(clothingItem)
            showToast(R.string.on_added_message.toString())

            //После добавления пользователя перенаправляет на list
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else{
            showToast(R.string.empty_title_error.toString())
        }
    }

    fun EmptyStringCheck(vararg strings: String?): Boolean {
        /*
        *   Метод принимает неограниченное количество строк,
        *   представленных в виде массива vararg strings: String?.
        *   Затем он проверяет каждую строку на null в цикле
        */
        for (str in strings) {
            if (str == null) {
                return false
            }
        }
        return true
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
