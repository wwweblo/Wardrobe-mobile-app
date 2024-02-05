package com.example.coursework.fragments.update

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.coursework.R
import com.example.coursework.model.ClothingItem
import com.example.coursework.viewModel.ClothesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class UpdateFregment : Fragment() {

    private lateinit var view: View
    private lateinit var imageButton: ImageButton
    private lateinit var titleInput: EditText
    //private lateinit var seasonInput: EditText
    private lateinit var seasonSpinner: Spinner
    private lateinit var descriptionInput: EditText
    private lateinit var updateButton: Button
    private lateinit var deleteButton: FloatingActionButton
    private lateinit var backButton:FloatingActionButton

    private val args by navArgs<UpdateFregmentArgs>()
    private lateinit var mClothingItemView: ClothesViewModel
    private val IMAGE_DIRECTORY = "ClothingImages"
    private var currentImagePath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_update, container, false)

        mClothingItemView = ViewModelProvider(this).get(ClothesViewModel::class.java)

        //Инициализация компонентов
        imageButton = view.findViewById(R.id.update_imageButton)
        titleInput = view.findViewById(R.id.update_title_input)
        //seasonInput = view.findViewById(R.id.update_season_input)
        seasonSpinner = view.findViewById(R.id.update_season_spinner)
        descriptionInput = view.findViewById(R.id.update_description_input)
        updateButton = view.findViewById(R.id.update_update_button)
        deleteButton = view.findViewById(R.id.update_delete_button)
        backButton = view.findViewById(R.id.update_back_button)

        // Установка значений из базы данных
        val imagePath = args.currentClothingItem.image
        setImageToImageButton(imagePath)
        titleInput.setText(args.currentClothingItem.title)

        // Задаем значение в Spinner
        val spinnerAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.seasons,
            android.R.layout.simple_spinner_item
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        seasonSpinner.adapter = spinnerAdapter

        // Получаем массив строк из ресурсов
        val seasonsArray = resources.getStringArray(R.array.seasons)

        // Устанавливаем значение в Spinner, если оно присутствует в массиве
        val currentSeason = args.currentClothingItem.season
        if (seasonsArray.contains(currentSeason)) {
            val position = spinnerAdapter.getPosition(currentSeason)
            seasonSpinner.setSelection(position)
        } else {
            // Выводим ошибку, так как значение отсутствует в Spinner
            showToast(getString(R.string.season_spinner_error))
        }

        descriptionInput.setText(args.currentClothingItem.description)


        imageButton.setOnClickListener{
            openGalleryForImage()
        }

        updateButton.setOnClickListener{
            updateItem()
        }

        deleteButton.setOnClickListener{
            deleteItem()
        }

        backButton.setOnClickListener{
            findNavController().navigate(R.id.action_updateFregment_to_listFragment)
        }

        return view
    }


    private fun deleteItem() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_,_ ->
            finnalydeleteItem()
        }
        builder.setNegativeButton("No"){_,_ -> }
        builder.setTitle("Delete ${args.currentClothingItem.title}?")
        builder.setMessage("Are you shure you want to delete ${args.currentClothingItem.title}?")
        builder.create().show()
    }
    private fun finnalydeleteItem() {

        mClothingItemView.deleteClothingItem(args.currentClothingItem)
        showToast(getString(R.string.on_deleted_message))

        findNavController().navigate(R.id.action_updateFregment_to_listFragment)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Загружает изображение из указанного пути и устанавливает его на кнопку.
     *
     * @param imagePath Путь к изображению.
     */
    private fun setImageToImageButton(imagePath: String?){
        if (imagePath.isNullOrBlank()) {
            //Пользователь не поставил изображение
            showToast(getString(R.string.no_image_error))
        } else {
            val imageFile = File(imagePath)
            if (imageFile.exists()) {
                val imageBitmap = BitmapFactory.decodeFile(imagePath)
                imageButton.setImageBitmap(imageBitmap)
            } else {
                //Файл отсутствует по пути
                showToast(getString(R.string.missed_image_error))
            }
        }
    }

    /**
     * Открывает галерею для выбора изображения.
     */
    private fun openGalleryForImage() {
        // Создаем intent для открытия изображений на устройстве
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    /**
     * Обработчик результата выбора изображения из галереи.
     */
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // Проверяем, что результат успешен
        if (result.resultCode == Activity.RESULT_OK) {
            // Получаем данные из результата
            val data: Intent? = result.data
            data?.data?.let { uri ->
                try {
                    // Сохраняем изображение в базу данных и получаем путь к сохраненному файлу
                    currentImagePath = saveImageInPackage(uri)

                    // Проверяем, что путь не null, и загружаем изображение на кнопку
                    currentImagePath?.let { path ->
                        setImageToImageButton(path)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Сохраняет изображение в базе данных и возвращает путь к сохраненному файлу.
     *
     * @param uri Uri изображения, которое нужно сохранить.
     * @return Путь к сохраненному изображению.
     * @throws IOException Если произошла ошибка при обработке ввода/вывода.
     */
    @Throws(IOException::class)
    private fun saveImageInPackage(uri: Uri): String {
        // Открываем поток ввода для изображения
        val inputStream = requireActivity().contentResolver.openInputStream(uri)

        // Проверяем, что поток ввода не null
        inputStream?.use {
            // Декодируем изображение из потока ввода
            val selectedImageBitmap = BitmapFactory.decodeStream(it)

            // Создаем папку, если ее нет
            val storageDir = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY)
            if (!storageDir.exists()) {
                storageDir.mkdirs()
            }

            // Создаем временный файл для изображения
            val imageFile = File.createTempFile("IMG_", ".jpg", storageDir)

            // Сохраняем выбранное изображение в файл
            val outputStream = FileOutputStream(imageFile)
            selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()

            // Возвращаем путь к сохраненному изображению
            return imageFile.absolutePath
        } ?: throw IOException("Input stream is null")
    }

    private fun emptyStringCheck(vararg strings: String?): Boolean {
        // Проверяем каждую строку
        for (str in strings) {
            // Если строка null или пуста, возвращаем false
            if (str == null || str.isBlank()) {
                return false
            }
        }
        // Если все строки прошли проверку, возвращаем true
        return true
    }

    /**
     * Вставляет данные в базу данных, используя значения из полей ввода на экране.
     *
     * @param view Представление фрагмента.
     */
    private fun updateItem(){
        val title = titleInput.text.toString()
        val season = seasonSpinner.selectedItem.toString()
        val description = descriptionInput.text.toString()

        if(emptyStringCheck(title)){

            val updatedClothingItem = ClothingItem(
                id = args.currentClothingItem.id,
                image = currentImagePath,
                title = title,
                season = season,
                description = description,

                dateUpdated = System.currentTimeMillis()
            )

            mClothingItemView.updateClothingItem(updatedClothingItem)
            showToast(getString(R.string.on_updated_message))

            findNavController().navigate(R.id.action_updateFregment_to_listFragment)
        }else{
            // Если title пуст или null, показываем ошибку
            showToast(getString(R.string.empty_title_error))
        }
    }
}
