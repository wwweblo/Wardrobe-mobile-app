package com.example.coursework.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.coursework.data.ClothesDatabase
import com.example.coursework.repository.ClothesRepository
import com.example.coursework.model.ClothingItem
import com.example.coursework.model.ClothingItemOutfitCrossRef
import com.example.coursework.model.Outfit
import com.example.coursework.repository.ClothingItemOutfitCrossRefRepository
import com.example.coursework.repository.OutfitRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*
*   Цель ViewModel - Работа с пользовательским интерфейсом
*/

class ClothesViewModel(application: Application) : AndroidViewModel(application) {

    // LiveData для списка элементов одежды
    var readAllClothes: LiveData<List<ClothingItem>> = MutableLiveData<List<ClothingItem>>()

    // LiveData для списка комплектов
    var readAllOutfits: LiveData<List<Outfit>> = MutableLiveData<List<Outfit>>()

    // Репозитории для работы с элементами одежды и комплектами
    private val clothesRepository: ClothesRepository
    private val outfitRepository: OutfitRepository
    private val clothingItemOutfitCrossRefRepository: ClothingItemOutfitCrossRefRepository

    var currentClothesSortType: SortType = SortType.BY_DATE_UPDATED
    var currentOutfitsSortType: SortType = SortType.BY_DATE_UPDATED

    init {
        //  Получаем dao из базы данных
        val clothesDao = ClothesDatabase.getDatabase(application).clothesDao()
        val outfitDao = ClothesDatabase.getDatabase(application).outfitDao()
        val clothesOutfitDao = ClothesDatabase.getDatabase(application).clothesItemOutfitDao()

        //  Передаем dao в репозитории
        clothesRepository = ClothesRepository(clothesDao)
        outfitRepository = OutfitRepository(outfitDao)
        clothingItemOutfitCrossRefRepository =
            ClothingItemOutfitCrossRefRepository(clothesOutfitDao)

        //  Получение списка Clothes
        readAllClothes = when (currentOutfitsSortType) {
            SortType.BY_TITLE -> clothesRepository.getClothingItemsSortedByTitle()
            SortType.BY_DATE_UPDATED -> clothesRepository.getClothingItemsSortedByDateUpdated()
        }
        // Получение списка Outfits
        readAllOutfits = when (currentClothesSortType) {
            SortType.BY_TITLE -> outfitRepository.getAllOutfitsSortedByName()
            SortType.BY_DATE_UPDATED -> outfitRepository.getAllOutfitsSortedByDate()
        }
    }


    // Clothes
    fun addClothingItem(clothingItem: ClothingItem) {
        viewModelScope.launch(Dispatchers.IO) {
            clothesRepository.addClothingItem(clothingItem)
        }
    }

    fun updateClothingItem(clothingItem: ClothingItem) {
        viewModelScope.launch(Dispatchers.IO) {
            clothesRepository.updateClothingItem(clothingItem)
        }
    }

    fun isClothesImagePathUsed(imagePath: String?): Boolean {
        return clothesRepository.isImagePathUsed(imagePath)
    }

    fun changeClothesSortType() {
        currentClothesSortType = when (currentClothesSortType) {
            SortType.BY_TITLE -> SortType.BY_DATE_UPDATED
            SortType.BY_DATE_UPDATED -> SortType.BY_TITLE
        }
        updateClothingItemsSortedBy(currentClothesSortType)
    }

    fun updateClothingItemsSortedBy(sortType: SortType) {
        currentClothesSortType = sortType
        readAllClothes = when (sortType) {
            SortType.BY_TITLE -> clothesRepository.getClothingItemsSortedByTitle()
            SortType.BY_DATE_UPDATED -> clothesRepository.getClothingItemsSortedByDateUpdated()
        }
    }

    fun deleteClothingItem(clothingItem: ClothingItem) {
        viewModelScope.launch(Dispatchers.IO) {
            clothingItemOutfitCrossRefRepository.deleteCrossRefsForClothingItem(clothingItem.id)
            clothesRepository.deleteClothingItem(clothingItem)
        }
    }

    fun toggleClothingItemSelection(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            clothesRepository.toggleClothingItemSelection(id)
        }
    }
    suspend fun isAnyItemSelected(): Boolean {
        return clothesRepository.isAnyItemSelected()
    }

    fun getSelectedClothingItemCount(): Int {
        return runBlocking(Dispatchers.IO) {
            clothesRepository.getSelectedClothingItemCount()
        }
    }

    fun deleteEveryClothingItem() {
        viewModelScope.launch(Dispatchers.IO) {
            clothesRepository.deleteEveryClothingItem()
        }
    }
    // Метод в ViewModel, который вызывает метод репозитория для удаления выбранных ClothingItem
    fun deleteSelectedClothingItems() {
        viewModelScope.launch(Dispatchers.IO) {
            clothesRepository.deleteSelectedClothingItems()
        }
    }


    // Outfits
    fun addOutfit(outfit: Outfit) {
        viewModelScope.launch(Dispatchers.IO) {
            outfitRepository.addOutfit(outfit)
        }
    }

    fun updateOutfit(outfit: Outfit) {
        viewModelScope.launch(Dispatchers.IO) {
            outfitRepository.updateOutfit(outfit)
        }
    }

    fun deleteOutfit(outfit: Outfit) {
        viewModelScope.launch(Dispatchers.IO) {
            clothingItemOutfitCrossRefRepository.deleteCrossRefsForOutfit(outfit.id)    //Удаляем связи перед удалением самой записи
            outfitRepository.deleteOutfit(outfit)
        }
    }

    fun changeOutfitSortType() {
        currentClothesSortType = when (currentClothesSortType) {
            SortType.BY_TITLE -> SortType.BY_DATE_UPDATED
            SortType.BY_DATE_UPDATED -> SortType.BY_TITLE
        }
        updateOutfitItemsSortedBy(currentClothesSortType)
    }

    fun updateOutfitItemsSortedBy(sortType: SortType) {
        currentOutfitsSortType = sortType
        readAllOutfits = when (sortType) {
            SortType.BY_TITLE -> outfitRepository.getAllOutfitsSortedByName()
            SortType.BY_DATE_UPDATED -> outfitRepository.getAllOutfitsSortedByDate()
            SortType.BY_DATE_UPDATED -> outfitRepository.getAllOutfitsSortedByDate()
        }
    }

    fun isOutfitImagePathUsed(imagePath: String?): Boolean {
        return outfitRepository.isImagePathUsed(imagePath)
    }


        // ClothesOutfit
    fun addClothingItemOutfitCrossRef(clothingItemOutfitCrossRef: ClothingItemOutfitCrossRef) {
        viewModelScope.launch(Dispatchers.IO) {
            clothingItemOutfitCrossRefRepository.addClothingItemOutfitCrossRef(
                clothingItemOutfitCrossRef
            )
        }
    }

    // Метод для получения списка элементов одежды для определенного комплекта
    fun getClothingItemsForOutfit(outfitId: Int): LiveData<List<ClothingItem>> {
        return clothingItemOutfitCrossRefRepository.getClothingItemsForOutfit(outfitId)
    }


    fun deleteClothingItemOutfitCrossRef(clothingItemOutfitCrossRef: ClothingItemOutfitCrossRef) {
        viewModelScope.launch(Dispatchers.IO) {
            clothingItemOutfitCrossRefRepository.deleteClothingItemOutfitCrossRef(
                clothingItemOutfitCrossRef
            )
        }
    }
}
