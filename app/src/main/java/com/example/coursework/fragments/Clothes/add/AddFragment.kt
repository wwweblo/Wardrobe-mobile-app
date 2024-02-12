package com.example.coursework.fragments.Clothes.add

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.coursework.R
import com.example.coursework.viewModel.ClothesViewModel
import com.example.coursework.model.ClothingItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AddFragment : Fragment() {

    private lateinit var mClothingItemView: ClothesViewModel
    private lateinit var imageButton: ImageButton
    private val IMAGE_DIRECTORY = "ClothingImages"
    private var currentImagePath: String? = null

    private lateinit var titleInput: EditText
    private lateinit var seasonSpinner: Spinner
    private lateinit var typeImput:EditText
    private lateinit var descriptionInput: EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        mClothingItemView = ViewModelProvider(this).get(ClothesViewModel::class.java)

        setupSeasonSpinner(view)

        titleInput = view.findViewById(R.id.add_title_input)
        seasonSpinner = view.findViewById(R.id.add_season_spinner)
        typeImput = view.findViewById(R.id.add_type_input)
        descriptionInput = view.findViewById(R.id.add_description_input)


        imageButton = view.findViewById(R.id.add_imageButton)
        imageButton.setOnClickListener {
            handleImageSelection()
        }

        view.findViewById<FloatingActionButton>(R.id.add_back_button).setOnClickListener {
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }

        view.findViewById<Button>(R.id.add_add_button).setOnClickListener {
            insertDataToDatabase(view)
        }

        return view
    }

    private fun setupSeasonSpinner(view: View) {
        val seasonSpinner = view.findViewById<Spinner>(R.id.add_season_spinner)
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.seasons,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        seasonSpinner.adapter = adapter
        seasonSpinner.setSelection(adapter.getPosition("All year"))
    }

    private fun handleImageSelection() {
        if (currentImagePath != null) {
            replaceCurrentImage(currentImagePath)
        }
        openGalleryForImage()
    }

    private fun replaceCurrentImage(imagePath: String?) {
        lifecycleScope.launch(Dispatchers.IO) {
            val isImagePathUsed = mClothingItemView.isClothesImagePathUsed(currentImagePath)
            if (!isImagePathUsed) {
                deleteImage(currentImagePath)
            }
        }
        showToast("Previous image is cleared")
    }

    private fun deleteImage(imagePath: String?) {
        imagePath?.takeIf { it.isNotBlank() }?.let { nonNullOrBlankImagePath ->
            val imageFile = File(nonNullOrBlankImagePath)
            if (imageFile.exists()) {
                imageFile.delete()
            }
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

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

    @Throws(IOException::class)
    private fun saveImageToDatabase(uri: Uri): String {
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        return inputStream?.use { stream ->
            val selectedImageBitmap = BitmapFactory.decodeStream(stream)
            val storageDir = File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                "Wardrobe app"
            )
            if (!storageDir.exists()) {
                storageDir.mkdirs()
            }
            val imageFile = File.createTempFile("IMG_", ".png", storageDir)
            val outputStream = FileOutputStream(imageFile)
            selectedImageBitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream)
            outputStream.close()
//            scanFile(imageFile) // Сканируем новый файл
            imageFile.absolutePath
        } ?: throw IOException("Input stream is null")
    }

    private fun scanFile(file: File) {
        MediaScannerConnection.scanFile(
            requireContext(),
            arrayOf(file.absolutePath),
            null,
            null
        )
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setImageToImageButton(imagePath: String) {
        val imageFile = File(imagePath)
        if (imageFile.exists()) {
            val imageBitmap = BitmapFactory.decodeFile(imagePath)
            imageButton.setImageBitmap(imageBitmap)
        } else {
            showToast(getString(R.string.missed_image_error))
        }
    }

    private fun insertDataToDatabase(view: View) {
        val title = titleInput.text.toString().takeIf { it.isNotBlank() } ?: "No title"
        val season = seasonSpinner.selectedItem.toString()
        val type = typeImput.text.toString().takeIf { it.isNotBlank() } ?: "No type"
        val description = descriptionInput.text.toString().takeIf { it.isNotBlank() } ?: "No description"

        if (emptyStringCheck(title)) {
            val clothingItem = ClothingItem(
                id = 0,
                image = currentImagePath,
                title = title,
                type = type,
                season = season,
                description = description,
                dateUpdated = System.currentTimeMillis()
            )
            mClothingItemView.addClothingItem(clothingItem)
            showToast(getString(R.string.on_added_message))
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            showToast(getString(R.string.empty_title_error))
        }
    }

    private fun emptyStringCheck(vararg strings: String?): Boolean {
        return strings.all { !it.isNullOrBlank() }
    }

    private fun showNotification(message: String) {
        val notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "Your_Channel_ID"
            val channel = NotificationChannel(
                channelId,
                "Your_Channel_Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)

            val notification = NotificationCompat.Builder(requireContext(), channelId)
                .setContentTitle("Изображение сохранено")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification)
                .build()

            notificationManager.notify(1, notification)
        } else {
            val notification = NotificationCompat.Builder(requireContext())
                .setContentTitle("Изображение сохранено")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification)
                .build()

            notificationManager.notify(1, notification)
        }
    }
}
