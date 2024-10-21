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
import com.zandroid.filimo_mvvm.data.models.category.ResponseCategory
import com.zandroid.filimo_mvvm.data.models.home.ResponseMovie
import com.zandroid.filimo_mvvm.data.models.home.ResponseMovie.ALLINONEVIDEO
import com.zandroid.filimo_mvvm.databinding.ItemCategoryBinding
import com.zandroid.filimo_mvvm.databinding.ItemMovieBinding
import com.zandroid.filimo_mvvm.databinding.ItemSliderMovieBinding
import com.zandroid.filimo_mvvm.utils.BaseDiffUtils
import com.zandroid.filimo_mvvm.utils.secToMin
import javax.inject.Inject

class CategoryAdapter @Inject constructor() : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private lateinit var binding: ItemCategoryBinding
    private var items = emptyList<ResponseCategory.ALLINONEVIDEO>()
    private lateinit var context:Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context=parent.context
        return ViewHolder()
    }




    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()


    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ResponseCategory.ALLINONEVIDEO) {
            binding.apply {

                //Img
                imgCategory.load(item.categoryImage){
                    crossfade(true)
                    crossfade(800)
                    memoryCachePolicy(CachePolicy.ENABLED)
                    error(R.drawable.ic_placeholder)
                }
                //Text
                txtCategory.text=item.categoryName


                //Click
                root.setOnClickListener {
                    onItemClickListener?.let { it(item) }
                }
            }
        }

    }




    private var onItemClickListener: ((ResponseCategory.ALLINONEVIDEO) -> Unit)? = null

    fun setOnItemClickListener(listener: (ResponseCategory.ALLINONEVIDEO) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<ResponseCategory.ALLINONEVIDEO>) {
        val adapterDiffUtils = BaseDiffUtils(items, data)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items = data
        diffUtils.dispatchUpdatesTo(this)
    }
}