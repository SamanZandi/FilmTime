package com.zandroid.filimo_mvvm.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.zandroid.filimo_mvvm.BuildConfig
import com.zandroid.filimo_mvvm.R
import com.zandroid.filimo_mvvm.databinding.ActivityMainBinding
import com.zandroid.filimo_mvvm.databinding.FragmentSplashBinding
import com.zandroid.filimo_mvvm.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

@AndroidEntryPoint
class SplashFragment : Fragment() {

    //Binding
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    //Others
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSplashBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            //Application Version
            versionTxt.text = "${getString(R.string.version)} ${BuildConfig.VERSION_NAME}  "


            imgBgSplash.load(R.drawable.cinema)
            imgMovie.load(R.drawable.splash_logo)
            initAnimation()
            lifecycleScope.launch {
                delay(3000)

                viewModel.readRegisterState.asLiveData().observe(viewLifecycleOwner){
                   findNavController().popBackStack(R.id.splashFragment, true)
                    if (it.email.isNotEmpty()){
                        findNavController().navigate(R.id.actionToHome)
                    }else{
                        findNavController().navigate(R.id.actionToRegister)
                    }
                }
            }



        }
    }


    private fun initAnimation(){

        val animation=ScaleAnimation(1f,1.5f,1f,1.5f,
            Animation.RELATIVE_TO_SELF,0.5f,
            Animation.RELATIVE_TO_SELF,0.5f)
        animation.duration=3000
        animation.repeatCount=3
        animation.isFillEnabled=true
       binding.imgMovie.startAnimation(animation)

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}