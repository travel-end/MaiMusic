package wind.widget.play.bg

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import wind.widget.R

/**
 * @By Journey 2020/10/29
 * @Description
 */
class BlurBgLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var layerDrawable: LayerDrawable? = null
    private lateinit var anim: ObjectAnimator

    companion object {
        const val anim_duration = 400
        const val index_background = 0
        const val index_foreground = 1
    }

    init {
        initLayerDrawable()
        initAnim()
    }
    private fun initLayerDrawable() {
        var backgroundDrawable: Drawable? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.temp_play_bg)
        }
        val drawables = arrayOfNulls<Drawable>(2)
        /*初始化时先将前景与背景颜色设为一致*/
        drawables[index_background] = backgroundDrawable
        drawables[index_foreground] = backgroundDrawable
        layerDrawable = LayerDrawable(drawables)
    }

    private fun initAnim() {
        anim = ObjectAnimator.ofFloat(this, "number", 0f, 1.0f)
        anim.duration = anim_duration.toLong()
        anim.interpolator = AccelerateInterpolator()
        anim.addUpdateListener(AnimatorUpdateListener { animation ->
            val foregroundAlpha = (animation.animatedValue as Float * 255).toInt()
            /*动态设置Drawable的透明度，让前景图逐渐显示*/
            layerDrawable!!.getDrawable(index_foreground).alpha = foregroundAlpha
            this@BlurBgLayout.background = layerDrawable
        })
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                /*动画结束后，记得将原来的背景图及时更新*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    layerDrawable!!.setDrawable(
                        index_background, layerDrawable!!.getDrawable(
                            index_foreground
                        )
                    )
                }
            }
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    fun setBgForeground(drawable:Drawable) {
        // android 6.0以上
        // TODO: 2020/10/29 处理6.0以下的方式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            layerDrawable?.setDrawable(index_foreground,drawable)
        }
    }
    fun beginAnimation() {
        anim.start()
    }
    fun clear() {
        if (anim.isRunning) {
            anim.cancel()
        }
    }
}