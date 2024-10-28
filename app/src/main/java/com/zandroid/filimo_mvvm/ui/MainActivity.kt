package com.zandroid.filimo_mvvm.ui

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.zandroid.filimo_mvvm.R
import com.zandroid.filimo_mvvm.databinding.ActivityMainBinding
import com.zandroid.filimo_mvvm.ui.splash.SplashFragment
import com.zandroid.filimo_mvvm.utils.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    //Binding
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var navHost:NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHost=supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        binding.bottomNav.background = null
        binding.bottomNav.setupWithNavController(navHost.navController)

        navHost.navController.addOnDestinationChangedListener{_,destination,_->
            when(destination.id){
                R.id.splashFragment->visibility(false)
                R.id.allMoviesFragment->visibility(false)
                R.id.registerFragment->visibility(false)
                R.id.detailFragment->visibility(false)
                R.id.playerFragment->visibility(false)
                else->visibility(true)

            }
        }
        binding.btnFab.setOnClickListener {
            navHost.navController.navigate(R.id.categoryFragment)
        }

        }

    private fun visibility(isVisible: Boolean){
        binding.apply {
            bottomAppBar.isVisible=if (isVisible) true else false
            btnFab.isVisible=if (isVisible) true else false
        }

    }




    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onNavigateUp(): Boolean {
        return super.onNavigateUp() || navHost.navController.navigateUp()
    }
}