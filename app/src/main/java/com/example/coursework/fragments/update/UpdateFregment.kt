package com.example.coursework.fragments.update

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.coursework.data.ClothesDatabase
import com.example.coursework.repository.ClothesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateFregment : Fragment() {

    private lateinit var view: View
    private lateinit var imageButton: ImageButton
    private lateinit var titleInput: EditText
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
            if (currentImagePath != null){
                replaceCurrentImage(currentImagePath)
            }
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


    // Метод для отображения диалога подтверждения удаления элемента
    private fun deleteItem() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            finallyDeleteItem()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete ${args.currentClothingItem.title}?")
        builder.setMessage("Are you sure you want to delete ${args.currentClothingItem.title}?")
        builder.create().show()
    }

    // Метод для выполнения удаления элемента после подтверждения
    private fun finallyDeleteItem() {
        val currentImagePath = args.currentClothingItem.image

        lifecycleScope.launch(Dispatchers.IO) {
            val isImagePathUsed = mClothingItemView.isImagePathUsed(currentImagePath)

            if (!isImagePathUsed) {
                deleteImage(currentImagePath)
            }

            mClothingItemView.deleteClothingItem(args.currentClothingItem)
        }

        showToast(getString(R.string.on_deleted_message))

        findNavController().navigate(R.id.action_updateFregment_to_listFragment)
    }

    // Метод для отображения сообщения в Toast
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    // Метод для установки изображения на ImageButton
    private fun setImageToImageButton(imagePath: String?) {
        if (imagePath.isNullOrBlank()) {
            showToast(getString(R.string.no_image_error))
        } else {
            val imageFile = File(imagePath)
            if (imageFile.exists()) {
                val imageBitmap = BitmapFactory.decodeFile(imagePath)
                imageButton.setImageBitmap(imageBitmap)
            } else {
                showToast(getString(R.string.missed_image_error))
            }
        }
    }

    // Метод для открытия галереи для выбора изображения
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    // Обработчик результата выбора изображения из галереи
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    try {
                        currentImagePath = saveImageToDatabase(uri)
                        currentImagePath?.let { path ->
                            setImageToImageButton(path)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }

    // Метод для сохранения изображения в базе данных и возврата пути к сохраненному файлу
    @Throws(IOException::class)
    private fun saveImageToDatabase(uri: Uri): String {
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        return inputStream?.use { stream ->
            val selectedImageBitmap = BitmapFactory.decodeStream(stream)
            val storageDir = File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                "Wardrobe_app"
            )
            if (!storageDir.exists()) {
                storageDir.mkdirs()
            }
            val imageFile = File.createTempFile("IMG_", ".png", storageDir)
            val outputStream = FileOutputStream(imageFile)
            selectedImageBitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream)
            outputStream.close()
            //showToast("Image size: ${imageFile.length() / 1024} KB")
            scanFile(imageFile)
            imageFile.absolutePath
        } ?: throw IOException("Input stream is null")
    }



    // Метод для обновления галереи после сохранения изображения
    private fun scanFile(file: File) {
        MediaScannerConnection.scanFile(
            requireContext(),
            arrayOf(file.absolutePath),
            null
        ) { _, _ -> }
    }

    // Метод для замены текущего изображения
    private fun replaceCurrentImage(imagePath: String?) {
        lifecycleScope.launch(Dispatchers.IO) {
            val isImagePathUsed = mClothingItemView.isImagePathUsed(currentImagePath)

            if (!isImagePathUsed) {
                deleteImage(currentImagePath)
            }
        }
        showToast("Previous image is cleared")
    }

    // Метод для удаления изображения по указанному пути
    private fun deleteImage(imagePath: String?) {
        imagePath?.takeIf { it.isNotBlank() }?.let { nonNullOrBlankImagePath ->
            val imageFile = File(nonNullOrBlankImagePath)
            if (imageFile.exists()) {
                imageFile.delete()
            }
        }
    }

    // Метод для проверки, что переданные строки не являются null и не являются пустыми или состоят только из пробелов
    private fun emptyStringCheck(vararg strings: String?): Boolean {
        return strings.all { it != null && it.isNotBlank() }
    }

    // Метод для обновления элемента в базе данных
    private fun updateItem() {
        val title = titleInput.text.toString()
        val season = seasonSpinner.selectedItem.toString()
        val description = descriptionInput.text.toString()

        if (emptyStringCheck(title)) {
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
        } else {
            showToast(getString(R.string.empty_title_error))
        }
    }

}
