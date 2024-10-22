package com.zandroid.filimo_mvvm.ui.category

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.zandroid.filimo_mvvm.R
import com.zandroid.filimo_mvvm.adapters.MoviesAdapter
import com.zandroid.filimo_mvvm.data.models.category.ResponseCategory
import com.zandroid.filimo_mvvm.databinding.FragmentCategoryBinding
import com.zandroid.filimo_mvvm.databinding.FragmentMovieByCategoryBinding
import com.zandroid.filimo_mvvm.ui.detail.DetailFragmentDirections
import com.zandroid.filimo_mvvm.utils.NetworkChecker
import com.zandroid.filimo_mvvm.utils.NetworkRequest
import com.zandroid.filimo_mvvm.utils.onceObserve
import com.zandroid.filimo_mvvm.utils.setupRecyclerview
import com.zandroid.filimo_mvvm.utils.setupShimmer
import com.zandroid.filimo_mvvm.utils.showSnackBar
import com.zandroid.filimo_mvvm.viewmodel.CategoryViewModel
import com.zandroid.filimo_mvvm.viewmodel.MovieByCatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class MovieByCategoryFragment : Fragment() {

    //Binding
    private var _binding: FragmentMovieByCategoryBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var allMoviesAdapter: MoviesAdapter

    @Inject
    lateinit var networkChecker: NetworkChecker

    //Other
    private val viewModel: MovieByCatViewModel by viewModels()
    private val args:MovieByCategoryFragmentArgs by navArgs()
    private var catId=0
    private var categoryName by Delegates.notNull<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieByCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        catId=args.catId
        categoryName=args.categoryName

        binding.apply {
            //Back
            btnBack.setOnClickListener {
            findNavController().popBackStack(R.id.movieByCategoryFragment,true)
            findNavController().navigate(R.id.categoryFragment)}
            //set title
            txtCategoryName.text=categoryName


            //check Internet
            lifecycleScope.launch {
                networkChecker.checkNetworkAvailability().collect{state->
                    if (state){
                        //call Api
                        callMoviesByCatApi()
                    }else{
                        checkInternet(state)

                    }
                }
            }



        }
    }


    private fun checkInternet(hasNet:Boolean){
       binding.apply {
           if (hasNet){
                contentCons.isVisible=true
               internetConsLay.isVisible=false
           }else{
               contentCons.isVisible=false
               internetConsLay.isVisible=true
           }
       }
    }

    private fun callMoviesByCatApi(){
        viewModel.getMoviesByCategories(catId)
        viewModel.movieCategoryData.observe(viewLifecycleOwner){response->
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
                    binding.root.showSnackBar(response.message!!,
                        ContextCompat.getColor(requireContext(),R.color.philippineSilver))
                }
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


    private fun goToDetail(id: Int) {
        val direction = MovieByCategoryFragmentDirections.actionToDetail(id)
        findNavController().navigate(direction)
    }




    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}