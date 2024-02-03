package com.example.coursework.fragments.add

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
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
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.coursework.R
import com.example.coursework.viewModel.ClothesViewModel
import com.example.coursework.model.ClothingItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AddFragment : Fragment() {

    private lateinit var mClothingItemView: ClothesViewModel
    private lateinit var imageButton: ImageButton
    private val IMAGE_DIRECTORY = "ClothingImages"
    private var currentImagePath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        mClothingItemView = ViewModelProvider(this).get(ClothesViewModel::class.java)

        // Устанавливаем адаптер для Spinner
        val seasonSpinner = view.findViewById<Spinner>(R.id.add_season_spinner)
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.seasons,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        seasonSpinner.adapter = adapter

        // Устанавливаем "All year" как выбранный элемент по умолчанию
        val allYearIndex = adapter.getPosition("All year")
        seasonSpinner.setSelection(allYearIndex)

        // По нажатии на add_imageButton пользователь выбирает изображение
        imageButton = view.findViewById(R.id.add_imageButton)
        imageButton.setOnClickListener {
            openGalleryForImage()
        }

        //
        view.findViewById<FloatingActionButton>(R.id.add_back_button).setOnClickListener{
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }

        //По нажатии на add_add_button происходит добавление в базу данных
        view.findViewById<Button>(R.id.add_add_button).setOnClickListener {
            insertDataToDatabase(view)
        }

        return view
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
                    currentImagePath = saveImageToDatabase(uri)

                    // Проверяем, что путь не null, и загружаем изображение на кнопку
                    currentImagePath?.let { path ->
                        SetImageToImageButton(path)
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
    private fun saveImageToDatabase(uri: Uri): String {
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

    /**
     * Загружает изображение из указанного пути и устанавливает его на кнопку.
     *
     * @param imagePath Путь к изображению.
     */
    private fun SetImageToImageButton(imagePath: String) {
        // Создаем объект File для указанного пути
        val imageFile = File(imagePath)

        // Проверяем, существует ли файл по указанному пути
        if (imageFile.exists()) {
            // Если файл существует, декодируем изображение и устанавливаем его на кнопку
            val imageBitmap = BitmapFactory.decodeFile(imagePath)
            imageButton.setImageBitmap(imageBitmap)
        } else {
            // Если файл отсутствует, показываем сообщение об ошибке
            showToast(getString(R.string.missed_image_error))
        }
    }

    /**
     * Вставляет данные в базу данных, используя значения из полей ввода на экране.
     *
     * @param view Представление фрагмента.
     */
    private fun insertDataToDatabase(view: View) {
        // Получаем значения из полей ввода
        val title = view.findViewById<EditText>(R.id.add_title_input).text.toString()
        val seasonSpinner = view.findViewById<Spinner>(R.id.add_season_spinner)
        val season = seasonSpinner.selectedItem.toString()
        val description = view.findViewById<EditText>(R.id.add_description_input).text.toString()

        // Если title не null и не является пустым, добавляем clothingItem
        if (emptyStringCheck(title)) {
            // Создаем объект ClothingItem с полученными значениями
            val clothingItem = ClothingItem(
                id = 0,
                image = currentImagePath,
                title = title,
                season = season,
                description = description,
                updated_at = System.currentTimeMillis()
            )

            // Добавляем clothingItem в базу данных
            mClothingItemView.addClothingItem(clothingItem)

            // Отображаем уведомление об успешном добавлении
            showToast(getString(R.string.on_added_message))

            // После добавления пользователя перенаправляем на экран списка
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            // Если title пуст или null, показываем ошибку
            showToast(getString(R.string.empty_title_error))
        }
    }


    private fun showToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Проверяет, что переданные строки не являются null и не являются пустыми или состоят только из пробелов.
     *
     * @param strings Набор строк, которые нужно проверить.
     * @return true, если все строки не являются null и не являются пустыми или состоят только из пробелов, иначе false.
     */
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
     * Отображает уведомление в системе Android.
     *
     * @param message Сообщение, которое будет отображено в уведомлении.
     */
    private fun showNotification(message: String) {
        // Получаем менеджер уведомлений
        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Создаем канал уведомлений для Android 8 (Oreo) и выше
            val channelId = "Your_Channel_ID"
            val channel = NotificationChannel(
                channelId,
                "Your_Channel_Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)

            // Строим уведомление
            val notification = NotificationCompat.Builder(requireContext(), channelId)
                .setContentTitle("Изображение сохранено")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification)
                .build()

            // Отображаем уведомление
            notificationManager.notify(1, notification)
        } else {
            // Для версий Android ниже 8 (Oreo) строим уведомление без использования канала
            val notification = NotificationCompat.Builder(requireContext())
                .setContentTitle("Изображение сохранено")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification)
                .build()

            // Отображаем уведомление
            notificationManager.notify(1, notification)
        }
    }

}
