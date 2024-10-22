package com.zandroid.filimo_mvvm.adapters


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.zandroid.filimo_mvvm.R
import com.zandroid.filimo_mvvm.data.models.detail.ResponseSingleMovie
import com.zandroid.filimo_mvvm.data.models.detail.ResponseSingleMovie.ALLINONEVIDEO.Related
import com.zandroid.filimo_mvvm.databinding.ItemRelatedBinding
import com.zandroid.filimo_mvvm.utils.BaseDiffUtils
import javax.inject.Inject

class RelatedAdapter @Inject constructor() : RecyclerView.Adapter<RelatedAdapter.ViewHolder>() {

    private lateinit var binding: ItemRelatedBinding
    private var items = emptyList<Related>()
    private lateinit var context:Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemRelatedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context=parent.context
        return ViewHolder()
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()




    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Related) {
            binding.apply {

                //Img
                imgRelated.load(item.videoThumbnailB){
                    crossfade(true)
                    crossfade(800)
                    memoryCachePolicy(CachePolicy.ENABLED)
                    error(R.drawable.ic_placeholder)
                }
                //Text
                txtRelatedMovie.text=item.videoTitle


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

    fun setData(data: List<Related>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)

        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}