package com.zandroid.filimo_mvvm.ui.detail

import android.R.attr.scrollY
import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.request.CachePolicy
import com.zandroid.filimo_mvvm.R
import com.zandroid.filimo_mvvm.adapters.MoviesAdapter
import com.zandroid.filimo_mvvm.adapters.RelatedAdapter
import com.zandroid.filimo_mvvm.data.db.entity.FavoriteEntity
import com.zandroid.filimo_mvvm.data.models.detail.ResponseSingleMovie
import com.zandroid.filimo_mvvm.databinding.FragmentDetailBinding
import com.zandroid.filimo_mvvm.utils.NetworkChecker
import com.zandroid.filimo_mvvm.utils.NetworkRequest
import com.zandroid.filimo_mvvm.utils.isVisible
import com.zandroid.filimo_mvvm.utils.secToMin
import com.zandroid.filimo_mvvm.utils.setTint
import com.zandroid.filimo_mvvm.utils.setupRecyclerview
import com.zandroid.filimo_mvvm.utils.setupShimmer
import com.zandroid.filimo_mvvm.utils.showSnackBar
import com.zandroid.filimo_mvvm.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {

    //Binding
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var relatedAdapter: RelatedAdapter

    @Inject
    lateinit var networkChecker: NetworkChecker

    //Other
    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()
    private var movieId = 0
    private var isExistsCache = false
    private var isExistsInFavorite = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //get data
        movieId = args.id
        if (movieId > 0) {
            checkCache(movieId)
        }

        binding.apply {
            //back
            btnBack.setOnClickListener {
                findNavController().popBackStack(R.id.detailFragment, true)
                findNavController().navigate(R.id.homeFragment)
            }

            //motion
            scrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                if (oldScrollY>scrollY) {
                    consDetailsMotion.transitionToEnd()
                }else {
                    consDetailsMotion.transitionToStart()
                }

                 Log.e("scrollY: ", "$scrollY")
            }


        }

        //check Internet
        lifecycleScope.launch {
            networkChecker.checkNetworkAvailability().collect{state->
                delay(200)
                 if (!isExistsCache){
                     initInternetLayout(state)
                     if (state){
                         callDetailApi()
                     }
                 }
            }
        }


    }


    @SuppressLint("SetTextI18n")
    private fun callDetailApi() {
        viewModel.loadDetailsMovie(movieId)
        binding.apply {
            viewModel.detailLiveData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        progress.isVisible(true, consDetailsMotion)
                    }

                    is NetworkRequest.Success -> {
                        progress.isVisible(false, consDetailsMotion)
                        response.data?.let { data ->
                            initDetailsViews(data)
                        }
                    }

                    is NetworkRequest.Error -> {
                        progress.isVisible(false, consDetailsMotion)
                    }
                }

            }
        }

    }


    private fun loadDetailFromDb(){
        viewModel.readDetailsFromDb(movieId).observe(viewLifecycleOwner){
            binding.consDetailsMotion.isVisible=true
            initDetailsViews(it.response)
        }

    }



    @SuppressLint("SetTextI18n")
    private fun initDetailsViews(data: ResponseSingleMovie){
        val movie=data.aLLINONEVIDEO.get(0)
       binding.apply  {
           //check favorite
           checkExistInFavorite(movie.id!!.toInt())
           btnBookmark.setOnClickListener {
                if (isExistsInFavorite) {
                    deleteFavorite(data)
                    root.showSnackBar(getString(R.string.delete_from_favorite),R.color.scarlet)
                } else {
                    saveFavorite(data)
                    root.showSnackBar(getString(R.string.added_to_favorite),R.color.caribbean_green)
                }
           }
           //text
           appTitle.text = movie.videoTitle
           movieTitle.text = movie.videoTitle
           val htmlFormatter = HtmlCompat.fromHtml(
               movie.videoDescription!!,
               HtmlCompat.FROM_HTML_MODE_COMPACT
           )
           txtDescMovie.text = htmlFormatter
           txtCategory.text = movie.categoryName
           txtDuration.text = movie.videoDuration!!.secToMin()
           txtView.text = "${movie.totelViewer} بازدید"
           //Image
           imgPoster.load(movie.videoThumbnailB) {
               crossfade(true)
               crossfade(500)
               memoryCachePolicy(CachePolicy.ENABLED)
               error(R.drawable.ic_placeholder)
           }
           //Related
           relatedAdapter.setData(movie.related!!.filterNotNull())
           relatedList.setupRecyclerview(
               LinearLayoutManager
                   (requireContext(), LinearLayoutManager.HORIZONTAL, false),
               relatedAdapter
           )
           //click
           relatedAdapter.setOnItemClickListener {
               goToDetail(it)
           }
           //go to player
           btnPlay.setOnClickListener {
               val direction=DetailFragmentDirections.actionToPlayer(movie.videoUrl!!,movie.videoThumbnailB!!)
               findNavController().navigate(direction)
           }
       }
    }

    //go related to detail
    private fun goToDetail(id: Int) {
        val direction = DetailFragmentDirections.actionToDetail(id)
        findNavController().navigate(direction)
    }


    //check cache
    private fun checkCache(id:Int){
        viewModel.existsDetails(id)
        viewModel.existsDetailsLiveData.observe(viewLifecycleOwner){exist->
            isExistsCache=exist
            if (exist){
                binding.consDetailsMotion.isVisible=true
                loadDetailFromDb()
            }
        }
    }


    //Favorite
    private fun saveFavorite(entity: ResponseSingleMovie){
        val movie=FavoriteEntity(entity.aLLINONEVIDEO.get(0).id!!.toInt(),entity)
        viewModel.saveFavorite(movie)
        binding.btnBookmark.apply {
            setTint(R.color.white)
            setImageResource(R.drawable.baseline_bookmark_24)
        }
    }

    private fun deleteFavorite(entity: ResponseSingleMovie){
        val movie=FavoriteEntity(entity.aLLINONEVIDEO.get(0).id!!.toInt(),entity)
        viewModel.deleteFavorite(movie)
        binding.btnBookmark.apply {
            setTint(R.color.white)
            setImageResource(R.drawable.baseline_bookmark_border_24)
        }
    }

    private fun checkExistInFavorite(id: Int){
        viewModel.existInFavorite(id)
        viewModel.existFavoriteData.observe(viewLifecycleOwner){
            isExistsInFavorite=it
         binding.btnBookmark.apply {
             if (it) {
                 setTint(R.color.white)
                 setImageResource(R.drawable.baseline_bookmark_24)
             }else{
                 setTint(R.color.white)
                 setImageResource(R.drawable.baseline_bookmark_border_24)
             }
         }
        }
    }



    //Internet Layout
    private fun initInternetLayout(isConnected: Boolean) {
        binding.internetConsLay.isVisible = isConnected.not()
        binding.consDetailsMotion.isVisible=false
    }


        override fun onDestroy() {
            super.onDestroy()
            _binding = null
        }



}