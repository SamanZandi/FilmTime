package com.zandroid.filimo_mvvm.adapters


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.zandroid.filimo_mvvm.R
import com.zandroid.filimo_mvvm.data.db.entity.FavoriteEntity
import com.zandroid.filimo_mvvm.data.models.home.ResponseMovie
import com.zandroid.filimo_mvvm.data.models.home.ResponseMovie.ALLINONEVIDEO
import com.zandroid.filimo_mvvm.databinding.ItemMovieBinding
import com.zandroid.filimo_mvvm.databinding.ItemSliderMovieBinding
import com.zandroid.filimo_mvvm.utils.BaseDiffUtils
import com.zandroid.filimo_mvvm.utils.secToMin
import javax.inject.Inject

class FavoriteAdapter @Inject constructor() : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private lateinit var binding: ItemMovieBinding
    private var items = emptyList<FavoriteEntity>()
    private lateinit var context:Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context=parent.context
        return ViewHolder()
    }

    //Animation tools
    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.initAnimation()
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.clearAnimation()
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()


    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: FavoriteEntity) {
            binding.apply {

                //Img
                itemMovieImg.load(item.response.aLLINONEVIDEO[0].videoThumbnailB){
                    crossfade(true)
                    crossfade(800)
                    memoryCachePolicy(CachePolicy.ENABLED)
                    error(R.drawable.ic_placeholder)
                }
                //Text
                txtTitle.text=item.response.aLLINONEVIDEO[0].videoTitle
                txtDuration.text=item.response.aLLINONEVIDEO[0].videoDuration!!.secToMin()
                txtViews.text="${item.response.aLLINONEVIDEO[0].totelViewer} بازدید "
                txtCategory.text=item.response.aLLINONEVIDEO[0].categoryName

                //Click
                root.setOnClickListener {
                    onItemClickListener?.let { it(item.id) }
                }
            }
        }

        fun initAnimation(){
            binding.root.animation=AnimationUtils.loadAnimation(context,R.anim.item_anim)
        }

        fun clearAnimation(){
            binding.root.clearAnimation()
        }
    }




    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<FavoriteEntity>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}