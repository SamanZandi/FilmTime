package com.zandroid.filimo_mvvm.adapters


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.zandroid.filimo_mvvm.R
import com.zandroid.filimo_mvvm.data.models.home.ResponseMovie
import com.zandroid.filimo_mvvm.data.models.home.ResponseMovie.ALLINONEVIDEO
import com.zandroid.filimo_mvvm.databinding.ItemSliderMovieBinding
import com.zandroid.filimo_mvvm.utils.BaseDiffUtils
import com.zandroid.filimo_mvvm.utils.secToMin
import javax.inject.Inject

class HomeSliderAdapter @Inject constructor() : RecyclerView.Adapter<HomeSliderAdapter.ViewHolder>() {

    private lateinit var binding: ItemSliderMovieBinding
    private var items = emptyList<ALLINONEVIDEO>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemSliderMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = if (items.size<=15) items.size else 15

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ALLINONEVIDEO) {
            binding.apply {

                //Img
                imgMovie.load(item.videoThumbnailB){
                    crossfade(true)
                    crossfade(800)
                    memoryCachePolicy(CachePolicy.ENABLED)
                    error(R.drawable.ic_placeholder)
                }
                //Text
                txtTitle.text=item.videoTitle
                txtDuration.text= "| ${item.videoDuration!!.secToMin()}"

                txtView.text=item.totelViewer

                //Click
                root.setOnClickListener {
                    onItemClickListener?.let { it(item.id!!.toInt()) }
                }
            }
        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<ALLINONEVIDEO>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}