package wind.widget.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import wind.widget.R

/**
 * @By Journey 2020/10/26
 * @Description
 */

/**
 * Glide加载图片 可以指定圆弧角度
 * cornerType：圆角角度
 * todo: 自定义placeHolder 和error
 */
fun ImageView.loadImg(
    url: String,
    round: Float = 0f,
    cornerType: RoundedCornersTransformation.CornerType = RoundedCornersTransformation.CornerType.ALL,
    placeholder: Int = R.drawable.default_cover,
    error: Int = R.drawable.default_cover
) {

    if (round == 0f) {
        val option = RequestOptions()
            .placeholder(placeholder)
            .error(error)
        Glide
            .with(this.context)
            .load(url)
            .apply(option)
            .into(this)
    } else {
        val option = RequestOptions
            .bitmapTransform(RoundedCornersTransformation(round.toIntPx(), 0, cornerType))
            .placeholder(placeholder)
            .error(error)
        Glide
            .with(this.context)
            .load(url)
            .apply(option)
            .into(this)
    }
}

fun loadImg(
    context:Context,
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