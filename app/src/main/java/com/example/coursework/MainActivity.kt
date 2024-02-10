package com.example.coursework

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.coursework.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Находим FragmentContainerView
        val fragmentContainerView = findViewById<FragmentContainerView>(R.id.fragmentContainerView)

        // Получаем экземпляр NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        // Находим NavController
        navController = navHostFragment.navController

        // Настройка BottomNavigationView с NavController
        binding.bottomNavigation.setupWithNavController(navController)

        // Установка слушателя событий для BottomNavigationView
        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_list -> {
                    // Переключаемся на ListFragment
                    navController.navigate(R.id.listFragment)
                    true
                }
                R.id.navigation_outfits -> {
                    // Переключаемся на OutfitListFragment
                    navController.navigate(R.id.outfitListFragment)
                    true
                }
                else -> false
            }
        }

        // Скрываем BottomNavigationView при старте активити
        binding.bottomNavigation.visibility = if (navController.currentDestination?.id == R.id.listFragment
            || navController.currentDestination?.id == R.id.outfitListFragment
        ) {
            View.VISIBLE
        } else {
            View.GONE
        }

        // Слушатель изменения фрагментов для показа/скрытия BottomNavigationView
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigation.visibility = if (destination.id == R.id.listFragment
                || destination.id == R.id.outfitListFragment
            ) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}
