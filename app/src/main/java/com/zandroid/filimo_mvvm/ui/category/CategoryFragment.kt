package com.zandroid.filimo_mvvm.ui.category

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.zandroid.filimo_mvvm.R
import com.zandroid.filimo_mvvm.adapters.CategoryAdapter
import com.zandroid.filimo_mvvm.data.models.category.ResponseCategory
import com.zandroid.filimo_mvvm.databinding.FragmentCategoryBinding
import com.zandroid.filimo_mvvm.utils.NetworkRequest
import com.zandroid.filimo_mvvm.utils.onceObserve
import com.zandroid.filimo_mvvm.utils.setupRecyclerview
import com.zandroid.filimo_mvvm.utils.setupShimmer
import com.zandroid.filimo_mvvm.utils.showSnackBar
import com.zandroid.filimo_mvvm.viewmodel.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CategoryFragment : Fragment() {

    //Binding
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    //Other
   private val viewModel: CategoryViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callCategoriesApi()
        loadCategoriesFromDb()



    }


    private fun loadCategoriesFromDb(){
        initCategoryRecycler()
        viewModel.readCategoriesFromDb.onceObserve(viewLifecycleOwner){
            if (it.isNotEmpty()){
                binding.categoryShimmer.setupShimmer(false)
                it[0].responseCategory.aLLINONEVIDEO?.let {
                    categoryAdapter.setData(it)
                }
            }else{
                viewModel.getCategoryList()
            }


        }
    }


    private fun callCategoriesApi(){

        viewModel.categoriesLiveData.observe(viewLifecycleOwner){response->
            when(response){
                is NetworkRequest.Loading->{
                    binding.categoryShimmer.setupShimmer(true)
                }
                is NetworkRequest.Success->{
                    binding.categoryShimmer.setupShimmer(false)
                    response.data?.aLLINONEVIDEO.let {
                        categoryAdapter.setData(it!!)

                    }
                }
                is NetworkRequest.Error->{
                    binding.categoryShimmer.setupShimmer(false)
                    binding.root.showSnackBar(response.message!!,
                        ContextCompat.getColor(requireContext(),R.color.philippineSilver))
                }
            }
        }
    }

    private fun initCategoryRecycler(){
        binding.categoryList.setupRecyclerview(LinearLayoutManager(requireContext()),categoryAdapter)

        //click
        categoryAdapter.setOnItemClickListener {
            val direction=CategoryFragmentDirections.actionCategoryToMovieCategory(it.cid!!.toInt(),it.categoryName)
            findNavController().navigate(direction)
        }
    }







    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}