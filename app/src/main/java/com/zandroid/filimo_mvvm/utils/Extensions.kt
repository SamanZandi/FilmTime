package com.zandroid.filimo_mvvm.utils

import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar

//SnackBar
fun View.showSnackBar(message:String, color:Int){
    val snackbar= this.let { Snackbar.make(it,message, Snackbar.LENGTH_SHORT) }
    snackbar.setBackgroundTint(color)
    snackbar.show()
}

//Visibility
fun View.showVisibility(isShown:Boolean,view: View){
    if (isShown){
        this.isVisible=true
        view.isVisible=false
    }else{
        this.isVisible=false
        view.isVisible=true
    }
}

//RecyclerView
fun RecyclerView.setupRecyclerview(layoutManager: RecyclerView.LayoutManager,adapter:RecyclerView.Adapter<*> ){
    this.apply {
        this.layoutManager=layoutManager
        this.adapter=adapter
        this.setHasFixedSize(true)
    }
}

fun String.secToMin(): String {
    var time:String=""//mm:ss
    val units = this.split(":".toRegex()).dropLastWhile { it.isEmpty() }
        .toTypedArray() //will break the string up into an array
    val minutes = units[0].toInt() //first element
    val seconds = units[1].toInt() //second element

    val duration = 60 * minutes + seconds //add up our values
    time = if (duration>=60 ) {
        if (seconds<10){
            " ${minutes}:0${seconds} دقیقه"
        }else{
            " ${minutes}:${seconds} دقیقه"
        }
    } else " ${seconds} ثانیه"

    return time
}

fun <T> LiveData<T>.onceObserve(owner: LifecycleOwner, observe: Observer<T>) {
    observe(owner, object : Observer<T> {
        override fun onChanged(t: T) {
            removeObserver(this)
            observe.onChanged(t)
        }
    })
}


//shimmer
fun ShimmerFrameLayout.setupShimmer(isShownLoading: Boolean) {
    this.apply {
        if (isShownLoading) {
            startShimmer()
            this.isVisible=true
        } else{
            stopShimmer()
            this.isVisible=false

        }
    }
}


fun View.isVisible(isShownLoading: Boolean, container: View) {
    if (isShownLoading) {
        this.isVisible = true
        container.isVisible = false
    } else {
        this.isVisible = false
        container.isVisible = true
    }
}

