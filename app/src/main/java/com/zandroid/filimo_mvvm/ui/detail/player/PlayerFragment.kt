package com.zandroid.filimo_mvvm.ui.detail.player

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.navArgs
import coil.load
import com.zandroid.filimo_mvvm.databinding.FragmentPlayerBinding
import com.zandroid.filimo_mvvm.utils.isVisible
import kotlinx.coroutines.delay


class PlayerFragment : Fragment() {

    //Binding
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    //Other
    private val args:PlayerFragmentArgs by navArgs()
    private var movieUrl=""
    private var moviePoster=""
    private lateinit var player : ExoPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //init player
        movieUrl=args.movieUrl
        moviePoster=args.moviePoster
        player=ExoPlayer.Builder(requireContext()).build()


        binding.apply {
            val mediaItem = MediaItem.fromUri(movieUrl)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady=true
            player.play()

            player.addListener(object :Player.Listener{
                override fun onPlaybackStateChanged(state: Int) {
                    super.onPlaybackStateChanged(state)
                    when(state){
                        Player.STATE_BUFFERING->{
                          progressVisibility(true)

                        }
                        Player.STATE_READY->{
                            progressVisibility(false)
                        }

                        Player.STATE_ENDED -> {

                        }

                        Player.STATE_IDLE -> {
                        progressVisibility(true)
                        }
                    }
                }
            })

            playerView.useController=true
            playerView.player=player
            movieImg.load(moviePoster)
        }


    }

    private fun progressVisibility(isLoading: Boolean){
        binding.apply {
            if (isLoading){
                progress.isVisible(true,playerView)
                movieImg.isVisible(true,playerView)
            }  else{
                progress.isVisible(false,playerView)
                movieImg.isVisible(false,playerView)
            }
        }

    }



    override fun onResume() {
        super.onResume()
        // Force landscape orientation for this fragment
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    }

    override fun onPause() {
        super.onPause()
        // Revert to portrait orientation after leaving this fragment
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        player.release()

    }

}