package com.example.programmingc.presentation.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.programmingc.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.example.programmingc.databinding.ActivityMainScreenBinding
import com.example.programmingc.presentation.ui.menu.DialogFragmentLives
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController

        observeMenuEvents()
        setupNavigationDrawer()
        observeNavigationState()
        viewModel.checkAuthState()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeMenuEvents(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.navigationEvent.collect { event ->
                    withContext(Dispatchers.Main){
                        navigateTo(event)
                    }
                }
            }
        }
    }

    private fun navigateTo(event: MainViewModel.MenuNavigationEvent){
        try {
            when(event){
                MainViewModel.MenuNavigationEvent.ToDiamonds -> {
                    if (navController.currentDestination?.id != R.id.fragmentDiamonds){
                        navController.navigate(R.id.fragmentDiamonds)
                    }
                }
                MainViewModel.MenuNavigationEvent.ToLives -> {
                    showLivesDialog()
                }
            }
        }catch (e: Exception){
            handleNavigationError(e)
        }
    }

    private fun showLivesDialog(){
        val livesDialog = DialogFragmentLives()

        livesDialog.show(supportFragmentManager, "LivesDialog")
    }

    private fun setupNavigationDrawer(){
        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId){
                R.id.nav_courses ->navController.navigate(R.id.fragmentCourses)
                R.id.nav_diamonds->navController.navigate(R.id.fragmentDiamonds)
                R.id.nav_settings->navController.navigate(R.id.fragmentMainScreen)
                R.id.nav_logout->{
                    viewModel.signOut()
                    navController.navigate(R.id.fragment_auth)}
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    fun openDrawer() {
        if (!binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun observeNavigationState(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.authState.collect { state ->
                    withContext(Dispatchers.Main){
                        handleNavigationState(state)
                    }
                }
            }
        }
    }

    private fun handleNavigationState(state: MainViewModel.AuthState){
        try {
            when (state) {
                MainViewModel.AuthState.Loading -> {
                    // TODO skip intro for returning users
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
        }catch (e: Exception){
            handleStateError(e)
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun handleStateError(error: Throwable) {
        Toast.makeText(this, "State error: ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
    }

    private fun handleNavigationError(error: Throwable){
        Toast.makeText(this, "Navigation error: ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
    }
}