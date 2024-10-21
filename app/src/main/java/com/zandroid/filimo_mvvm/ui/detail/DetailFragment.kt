package com.zandroid.filimo_mvvm.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.request.CachePolicy
import com.zandroid.filimo_mvvm.R
import com.zandroid.filimo_mvvm.databinding.FragmentCategoryBinding
import com.zandroid.filimo_mvvm.databinding.FragmentDetailBinding
import com.zandroid.filimo_mvvm.utils.NetworkRequest
import com.zandroid.filimo_mvvm.utils.isVisible
import com.zandroid.filimo_mvvm.utils.secToMin
import com.zandroid.filimo_mvvm.viewmodel.DetailViewModel
import com.zandroid.filimo_mvvm.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    //Binding
    private var _binding:FragmentDetailBinding? = null
    private val binding get() = _binding!!

    //Other
    private val viewModel: DetailViewModel by viewModels()
    private val args : DetailFragmentArgs by navArgs()
    private var movieId=0


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
        movieId=args.id
        if (movieId>0){
            callDetailApi()
        }

        binding.apply {
            //back
            btnBack.setOnClickListener {
                findNavController().popBackStack(R.id.detailFragment,true)
                findNavController().navigate(R.id.homeFragment)
            }
            //motion
            scrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                if (scrollY>0){
                    consDetailsMotion.transitionToEnd()
                }else{
                    consDetailsMotion.transitionToStart()
                }
            }
        }

    }



    @SuppressLint("SetTextI18n")
    private fun callDetailApi(){
        viewModel.loadDetailsMovie(movieId)
        binding.apply {
            viewModel.detailLiveData.observe(viewLifecycleOwner){response->
                when(response){
                    is NetworkRequest.Loading->{
                        progress.isVisible(true,consDetailsMotion)
                    }
                    is NetworkRequest.Success->{
                        progress.isVisible(false,consDetailsMotion)
                        response.data?.let {data->
                            val movie=data.aLLINONEVIDEO?.get(0)!!
                            //text
                            appTitle.text= movie.videoTitle
                            movieTitle.text= movie.videoTitle
                            val htmlFormatter=HtmlCompat.fromHtml(movie.videoDescription!!,HtmlCompat.FROM_HTML_MODE_COMPACT)
                            txtDescMovie.text=htmlFormatter
                            txtCategory.text=movie.categoryName
                            txtDuration.text=movie.videoDuration!!.secToMin()
                            txtView.text="${movie.totelViewer} بازدید"
                            //Image
                            imgPoster.load(movie.videoThumbnailB){
                                crossfade(true)
                                crossfade(500)
                                memoryCachePolicy(CachePolicy.ENABLED)
                                error(R.drawable.ic_placeholder)
                            }


                        }
                    }
                    is NetworkRequest.Error->{
                        progress.isVisible(false,consDetailsMotion)
                    }
                }

            }
        }

    }




    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}