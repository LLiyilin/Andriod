package com.example.puremusic.bridge.data.binding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 * 这里和布局建立绑定
 * 注意：这个类的使用，居然是和 fragment_main.xml里面的onClickWithDebouncing 挂钩的
 */
@SuppressWarnings("unused")
object CommonBindingAdapter {
    // 要在 Kotlin 中使用数据绑定注释，请在模块的 build.gradle 中应用“kotlin-kapt”插件
    @JvmStatic
    @BindingAdapter(value = ["imageUrl", "placeHolder"], requireAll = false)
    fun loadUrl(view: ImageView, url: String?, placeHolder: Drawable?) {
        Glide.with(view.context).load(url).placeholder(placeHolder).into(view)
    }

}