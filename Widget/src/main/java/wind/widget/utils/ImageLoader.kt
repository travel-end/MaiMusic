package wind.widget.utils

import android.widget.ImageView
import androidx.constraintlayout.widget.Placeholder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
    placeholder: Int = R.drawable.place_holder,
    error:Int = R.drawable.place_holder
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