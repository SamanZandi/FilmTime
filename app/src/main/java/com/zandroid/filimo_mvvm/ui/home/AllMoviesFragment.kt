package com.zandroid.filimo_mvvm.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.zandroid.filimo_mvvm.R
import com.zandroid.filimo_mvvm.adapters.AllMoviesAdapter
import com.zandroid.filimo_mvvm.adapters.MoviesAdapter
import com.zandroid.filimo_mvvm.databinding.FragmentAllMoviesBinding
import com.zandroid.filimo_mvvm.databinding.FragmentHomeBinding
import com.zandroid.filimo_mvvm.utils.NetworkRequest
import com.zandroid.filimo_mvvm.utils.onceObserve
import com.zandroid.filimo_mvvm.utils.setupRecyclerview
import com.zandroid.filimo_mvvm.utils.showSnackBar
import com.zandroid.filimo_mvvm.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AllMoviesFragment : Fragment() {

    //Binding
    private var _binding: FragmentAllMoviesBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var allMoviesAdapter: MoviesAdapter

    //Other
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllMoviesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //load data
        callMoviesApi()
        loadAllMoviesData()

        binding.apply {
            //Back
            btnBack.setOnClickListener { findNavController().popBackStack() }



        }
    }

    //All Movies
    private fun callMoviesApi(){
        viewModel.allMovieData.observe(viewLifecycleOwner){response->
            when(response){
                is NetworkRequest.Loading->{
                    setupShimmer(true,binding.allMoviesShimmer)
                }
                is NetworkRequest.Success->{
                    setupShimmer(false,binding.allMoviesShimmer)
                    response.data?.let {movie->
                        if (movie.aLLINONEVIDEO!!.isNotEmpty()){
                            allMoviesAdapter.setData(movie.aLLINONEVIDEO)
                        }
                    }
                }
                is NetworkRequest.Error->{
                    setupShimmer(false,binding.allMoviesShimmer)
                    binding.root.showSnackBar(response.message!!,
                        ContextCompat.getColor(requireContext(),R.color.philippineSilver))
                }
            }
        }
    }

    private fun loadAllMoviesData(){
        initAllMoviesRecycler()
        viewModel.readAllMoviesFromDb.onceObserve(viewLifecycleOwner){db->
            if (db.isNotEmpty() && db.size>1){
                db[1].responseMovie.aLLINONEVIDEO?.let {
                    setupShimmer(false, binding.allMoviesShimmer)
                    allMoviesAdapter.setData(it)

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

        }
    }

    //shimmer
    private fun setupShimmer(isShownLoading: Boolean, shimmer: ShimmerFrameLayout) {
        shimmer.apply {
            if (isShownLoading) {
                startShimmer()
            } else{
                stopShimmer()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}