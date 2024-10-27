package com.zandroid.filimo_mvvm.ui.home

import android.graphics.Rect
import android.os.Bundle
import android.provider.SyncStateContract.Constants
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.appbar.MaterialToolbar
import com.zandroid.filimo_mvvm.R
import com.zandroid.filimo_mvvm.adapters.AllMoviesAdapter
import com.zandroid.filimo_mvvm.adapters.HomeSliderAdapter
import com.zandroid.filimo_mvvm.data.models.home.ResponseMovie
import com.zandroid.filimo_mvvm.databinding.ActivityMainBinding
import com.zandroid.filimo_mvvm.databinding.FragmentHomeBinding
import com.zandroid.filimo_mvvm.databinding.FragmentSplashBinding
import com.zandroid.filimo_mvvm.databinding.ItemMovieShimmerBinding
import com.zandroid.filimo_mvvm.databinding.ItemSliderMovieShimmerBinding
import com.zandroid.filimo_mvvm.ui.MainActivity
import com.zandroid.filimo_mvvm.ui.home.HomeFragmentDirections
import com.zandroid.filimo_mvvm.utils.DELAY_TIME
import com.zandroid.filimo_mvvm.utils.NetworkRequest
import com.zandroid.filimo_mvvm.utils.REPEAT_TIME
import com.zandroid.filimo_mvvm.utils.onceObserve
import com.zandroid.filimo_mvvm.utils.setupRecyclerview
import com.zandroid.filimo_mvvm.utils.setupShimmer
import com.zandroid.filimo_mvvm.utils.showSnackBar
import com.zandroid.filimo_mvvm.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.truncate

@AndroidEntryPoint
class HomeFragment : Fragment() {

    //Binding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sliderAdapter: HomeSliderAdapter

    @Inject
    lateinit var allMoviesAdapter: AllMoviesAdapter
    //Other
    private val viewModel:HomeViewModel by viewModels()
    private var autoScrollIndex=0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Api
        callSliderApi()
        callMoviesApi()
        //Local
        loadSliderData()
        loadAllMoviesData()


        binding.apply {
            //set actionBar
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            //motion
           scrollview.setOnScrollChangeListener { _, _, scrollY, _,_ ->
               if (scrollY>0){
                    rootMotion.transitionToEnd()
               }else{
                  rootMotion.transitionToStart()
               }
           }
            btnShowMore.setOnClickListener {
                val action=HomeFragmentDirections.actionHomeToAllMovies()
                findNavController().navigate(action)
            }

            btnSearch.setOnClickListener {
                findNavController().navigate(R.id.searchFragment)
            }


        }
    }

    //Banner
    private fun callSliderApi(){
        viewModel.latestLiveData.observe(viewLifecycleOwner){response->
            when(response){
                is NetworkRequest.Loading->{
                    binding.bannerShimmer.setupShimmer(true)
                }
                is NetworkRequest.Success->{
                    binding.bannerShimmer.setupShimmer(false)
                    response.data?.let {movie->
                        if (movie.aLLINONEVIDEO!!.isNotEmpty()){
                            sliderAdapter.setData(movie.aLLINONEVIDEO)
                            setupAutoScrollSlider(movie.aLLINONEVIDEO)
                        }
                    }
                }
                is NetworkRequest.Error->{
                    binding.bannerShimmer.setupShimmer(false)
                    binding.root.showSnackBar(response.message!!,R.color.philippineSilver)
                }
            }
        }

    }


    private fun loadSliderData(){
        initSliderRecycler()
        viewModel.readLatestMoviesFromDb.onceObserve(viewLifecycleOwner) { database ->
            if (database.isNotEmpty()) {
                database[0].responseMovie.aLLINONEVIDEO?.let { result ->
                    binding.bannerShimmer.setupShimmer(false)
                    sliderAdapter.setData(result)
                    setupAutoScrollSlider(result)
                }
            } else {
                viewModel.getLatestMovies()
            }
        }
    }

    private fun initSliderRecycler(){
        val snapHelper=LinearSnapHelper()
        binding.apply {
            bannerList.setupRecyclerview(LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false),sliderAdapter)
            snapHelper.attachToRecyclerView(bannerList)
        }
        //click
        sliderAdapter.setOnItemClickListener {
            goToDetail(it)
        }
    }


    private fun setupAutoScrollSlider(list:List<ResponseMovie.ALLINONEVIDEO>){
        lifecycleScope.launch {
            repeat(REPEAT_TIME){
                delay(DELAY_TIME)
                if(autoScrollIndex<list.size){
                    autoScrollIndex++
                }else{
                    autoScrollIndex=0
                }
                binding.bannerList.smoothScrollToPosition(autoScrollIndex)
            }

        }
    }


    //All Movies
    private fun callMoviesApi(){
        viewModel.allMovieData.observe(viewLifecycleOwner){response->
            when(response){
                is NetworkRequest.Loading->{
                    binding.allMoviesShimmer.setupShimmer(true)
                }
                is NetworkRequest.Success->{
                    binding.allMoviesShimmer.setupShimmer(false)
                    response.data?.let {movie->
                        if (movie.aLLINONEVIDEO!!.isNotEmpty()){
                            allMoviesAdapter.setData(movie.aLLINONEVIDEO)
                            initAllMoviesRecycler()
                        }
                    }
                }
                is NetworkRequest.Error->{
                    binding.allMoviesShimmer.setupShimmer(false)
                    binding.root.showSnackBar(response.message!!,ContextCompat.getColor(requireContext(),R.color.philippineSilver))
                }
            }
        }
    }

    private fun loadAllMoviesData(){

        viewModel.readAllMoviesFromDb.onceObserve(viewLifecycleOwner){db->
            if (db.isNotEmpty() && db.size>1 ){
                db[1].responseMovie.aLLINONEVIDEO?.let {
                    binding.allMoviesShimmer.setupShimmer(false)
                    allMoviesAdapter.setData(it)
                    initAllMoviesRecycler()
                    Log.e( "loadAllMoviesData: ", "$it")
                }
            }else{
                viewModel.getAllMovies()
            }

        }
    }

    private fun initAllMoviesRecycler(){
        binding.apply {
            moviesList.setupRecyclerview(LinearLayoutManager(requireContext()),allMoviesAdapter)
        }
        //click
        allMoviesAdapter.setOnItemClickListener {
                goToDetail(it)
        }
    }

    private fun goToDetail(id:Int){
        val direction=HomeFragmentDirections.actionToDetail(id)
        findNavController().navigate(direction)
    }




    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}