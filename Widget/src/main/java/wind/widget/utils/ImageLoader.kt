package wind.widget.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import wind.widget.R

/**
 * @By Journey 2020/10/26
 * @Description
 */
// TODO: 2020/11/19 不能所有的error都使用default_cover
fun ImageView.loadImg(
    url: String,
    placeholder: Int = R.drawable.default_cover,
    error: Int = R.drawable.default_cover
) {
    val option = RequestOptions()
        .placeholder(placeholder)
        .error(error)
    Glide
        .with(this.context)
        .load(url)
        .apply(option)
        .into(this)
}

fun loadImg(
    context: Context,
    url: String,
    placeholder: Int = R.drawable.default_cover,
    error: Int = R.drawable.default_cover,
    block: (resource: Drawable) -> Unit
) {
    Glide
        .with(context)
        .load(url)
        .apply(RequestOptions.placeholderOf(placeholder).error(error))
        .into(object : SimpleTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                block.invoke(resource)
            }
        })
}

fun ImageView.loadImgWithError(url: String,errorBlock:()->Unit) {
    Glide.with(this.context)
        .load(url)
        .listener(object :RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                errorBlock.invoke()
                return true
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

        })
        .into(this)

}