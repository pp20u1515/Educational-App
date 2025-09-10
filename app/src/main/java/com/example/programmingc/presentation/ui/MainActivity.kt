package com.example.programmingc.presentation.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.programmingc.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.example.programmingc.databinding.ActivityMainScreenBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainScreenBinding? = null
    private val binding: ActivityMainScreenBinding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            activityVM = viewModel
            lifecycleOwner = this@MainActivity
            //executePendingBindings()
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController

        setupNavigationDrawer()
        viewModel.checkAuthState()
    }

    override fun onResume() {
        super.onResume()

        restoreNavigationState()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupNavigationDrawer(){
        //binding.navigationView.setCheckedItem(selectedItemId)
        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId){
                R.id.nav_courses ->navController.navigate(R.id.fragmentLevels)
                R.id.nav_diamonds->navController.navigate(R.id.fragmentDiamonds)
                R.id.nav_settings->navController.navigate(R.id.fragmentMainScreen)
                R.id.nav_levels->navController.navigate(R.id.fragmentLevels)
                R.id.nav_logout->{
                    viewModel.signOut()
                    navController.navigate(R.id.fragment_auth)}
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun openDrawer() {
        if (!binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun restoreNavigationState(){
        lifecycleScope.launch {
            viewModel.authState.collect { state ->
                when (state) {
                    MainViewModel.AuthState.Loading -> {
                        // TODO
                    }
                    MainViewModel.AuthState.Authenticated -> {
                        val currentDestination = navController.currentDestination?.id

                        if (currentDestination == null || currentDestination == R.id.fragment_auth) {
                            navController.navigate(R.id.fragmentMainScreen)
                        }
                        viewModel.showMenu()
                    }

                    MainViewModel.AuthState.Unauthenticated -> {
                        val currentDestination = navController.currentDestination?.id

                        if (currentDestination != R.id.fragment_auth) {
                            navController.navigate(R.id.fragment_auth)
                        }
                        viewModel.hideMenu()
                    }

                    is MainViewModel.AuthState.Error -> showError(state.message)
                }
            }
        }
    }
}