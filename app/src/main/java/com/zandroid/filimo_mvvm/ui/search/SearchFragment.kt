package com.zandroid.filimo_mvvm.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zandroid.filimo_mvvm.R
import com.zandroid.filimo_mvvm.adapters.MoviesAdapter
import com.zandroid.filimo_mvvm.databinding.FragmentFavoriteBinding
import com.zandroid.filimo_mvvm.databinding.FragmentSearchBinding
import com.zandroid.filimo_mvvm.ui.favorite.FavoriteFragmentDirections
import com.zandroid.filimo_mvvm.utils.NetworkChecker
import com.zandroid.filimo_mvvm.utils.NetworkRequest
import com.zandroid.filimo_mvvm.utils.isVisible
import com.zandroid.filimo_mvvm.utils.setupRecyclerview
import com.zandroid.filimo_mvvm.utils.setupShimmer
import com.zandroid.filimo_mvvm.utils.showSnackBar
import com.zandroid.filimo_mvvm.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class SearchFragment : Fragment() {

    //Binding
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var moviesAdapter: MoviesAdapter

    @Inject
    lateinit var networkChecker: NetworkChecker

    //Other
    private val viewModel:SearchViewModel by viewModels()
    private var isNetworkAvailable by Delegates.notNull<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel.searchListLiveData.observe(viewLifecycleOwner){response->
                when(response){
                    is NetworkRequest.Loading->{
                        searchMoviesShimmer.setupShimmer(true)
                    }
                    is NetworkRequest.Success->{
                        searchMoviesShimmer.setupShimmer(false)
                        response.data!!.aLLINONEVIDEO?.let {data->
                        if (data.isNotEmpty()){
                            emptyConsLayout.isVisible(false,searchList)
                            moviesAdapter.setData(data)
                            searchList.setupRecyclerview(LinearLayoutManager(requireContext()),moviesAdapter)
                            moviesAdapter.setOnItemClickListener {
                                goToDetails(it)
                            }
                        }
                        }
                    }
                    is NetworkRequest.Error->{
                        searchMoviesShimmer.setupShimmer(false)
                        binding.root.showSnackBar(response.message!!, R.color.philippineSilver)
                    }
                }

            }

            //check Internet
            lifecycleScope.launch {
                networkChecker.checkNetworkAvailability().collect{state->
                    isNetworkAvailable=state
                    if (state){
                        emptyConsLayout.isVisible(false,searchList)
                    }else{
                        searchMoviesShimmer.setupShimmer(false)
                        delay(200)
                        emptyConsLayout.isVisible(true,searchList)

                    }
                }
            }

            searchEdt.addTextChangedListener {searchTxt->
                if (searchTxt.toString().length>2 && isNetworkAvailable){
                    viewModel.searchMovie(searchTxt.toString())
                }else{
                    searchMoviesShimmer.setupShimmer(false)
                    emptyConsLayout.isVisible(true,searchList)
                    emptyList.txtNoNet.text= getString(R.string.not_found)
                    emptyList.txtNoNet.setCompoundDrawablesWithIntrinsicBounds(null,resources.getDrawable(R.drawable.empty_box_icon),null,null)
                }
            }


        }

    }


    private fun goToDetails(id:Int){
        val direction= FavoriteFragmentDirections.actionToDetail(id)
        findNavController().navigate(direction)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}