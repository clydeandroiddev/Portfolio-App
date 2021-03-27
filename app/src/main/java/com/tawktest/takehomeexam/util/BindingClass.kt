package com.tawktest.takehomeexam.util

import android.graphics.drawable.shapes.RoundRectShape
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso



@BindingAdapter("app:imageUri")
fun loadImageWithUri(imageView: ImageView, imageUri: String?){
    if(!imageUri.isNullOrEmpty()){
        Picasso.get()
            .load(imageUri)
            .into(imageView)

    }

}
