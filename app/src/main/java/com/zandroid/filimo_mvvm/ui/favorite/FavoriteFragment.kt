package com.zandroid.filimo_mvvm.ui.favorite

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.zandroid.filimo_mvvm.R
import com.zandroid.filimo_mvvm.adapters.FavoriteAdapter
import com.zandroid.filimo_mvvm.databinding.FragmentDetailBinding
import com.zandroid.filimo_mvvm.databinding.FragmentFavoriteBinding
import com.zandroid.filimo_mvvm.ui.category.CategoryFragmentDirections
import com.zandroid.filimo_mvvm.utils.isVisible
import com.zandroid.filimo_mvvm.utils.setupRecyclerview
import com.zandroid.filimo_mvvm.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    //Binding
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var favoriteAdapter: FavoriteAdapter

    //Other
    private val viewModel:FavoriteViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFavoriteList()
    }



    @SuppressLint("ResourceType", "UseCompatLoadingForDrawables")
    private fun loadFavoriteList(){
        viewModel.readFavoriteData.observe(viewLifecycleOwner){favorites->
         binding.apply {
             if (favorites.isNotEmpty()){
                 emptyConsLayout.isVisible(false,favoriteList)
                 favoriteAdapter.setData(favorites)
                 favoriteList.setupRecyclerview(LinearLayoutManager(requireContext()),favoriteAdapter)
                 //click
                 favoriteAdapter.setOnItemClickListener {
                     goToDetails(it)
                 }
             }else{
                 emptyConsLayout.isVisible(true,favoriteList)
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