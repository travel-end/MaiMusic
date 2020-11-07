package wind.maimusic.utils

import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import wind.maimusic.Constants
import wind.maimusic.MaiApp
import wind.maimusic.R
import wind.maimusic.ui.activities.PlayActivity
import wind.widget.cost.Consts
import wind.widget.utils.toIntPx
import java.lang.reflect.ParameterizedType
import java.util.*

/**
 * @By Journey 2020/10/25
 * @Description
 */

/**
 * 通过反射获取父类泛型<T>对应的Class类
 */
@Suppress("UNCHECKED_CAST")
fun <T> getClass(t:Any):Class<T> = (t.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>

/**
 * 获取字符串资源
 */
fun Int.getStringRes()=
    MaiApp.getInstance().resources.getString(this)

fun String?.getEditableStr() :Editable {
    val value = this ?: ""
    return SpannableStringBuilder(value)
}


/**
 * 获取颜色资源
 */
fun Int.getColorRes()=
    MaiApp.getInstance().resources.getColor(this)

/**
 * 字符串不为null或者空
 */
fun String?.isNotNullOrEmpty():Boolean = !(this == null || this.trim().isBlank())

/**
 * 列表不为null或者空
 */
fun isNotNullOrEmpty(list:List<Any>?) :Boolean{
    return !list.isNullOrEmpty()
}

/**
 * toast
 */
fun CharSequence.toast(duration:Int = Toast.LENGTH_SHORT) {
    if (this.isNotEmpty()) {
        Toast.makeText(MaiApp.getInstance(),this,duration).show()
    }
}

/**
 * 加载xml布局
 */
fun Int.inflate(parent:ViewGroup,attachToRoot:Boolean = false) :View{
    return LayoutInflater.from(parent.context).inflate(this,parent,attachToRoot)
}

/**
 * 显示view
 */
fun View?.visible() {
    if (this?.visibility == View.GONE) {
        this.visibility = View.VISIBLE
    }
}

/**
 * 显示view，带有渐显得动画效果
 */
fun View?.visibleWithAlphaAnim(duration:Long = 500L) {
    this?.visibility = View.VISIBLE
    this?.startAnimation(
        AlphaAnimation(0f,1f).apply {
            this.duration = duration
            fillAfter = true
        }
    )
}

/**
 * 隐藏view
 */
fun View?.gone() {
    if (this?.visibility == View.VISIBLE) {
        this.visibility = View.GONE
    }
}

/**
 * 占位隐藏view
 */
fun View?.invisible() {
    this?.visibility = View.INVISIBLE
}

/**
 * 隐藏view，带有渐隐动画效果。
 *
 * @param duration 毫秒，动画持续时长，默认500毫秒。
 */
fun View?.goneWithAlphaAnim(duration: Long = 500L) {
    this?.visibility = View.GONE
    this?.startAnimation(AlphaAnimation(1f, 0f).apply {
        this.duration = duration
        fillAfter = true
    })
}

/**
 * 占位隐藏view，带有渐隐动画效果。
 *
 * @param duration 毫秒，动画持续时长，默认500毫秒。
 */
fun View?.invisibleWithAlphaAnim(duration: Long = 500L) {
    this?.visibility = View.INVISIBLE
    this?.startAnimation(AlphaAnimation(1f, 0f).apply {
        this.duration = duration
        fillAfter = true
    })
}

/**
 * 设置TextView图标
 *
 * @param drawable     图标
 * @param iconWidth    图标宽dp：默认自动根据图标大小
 * @param iconHeight   图标高dp：默认自动根据图标大小
 * @param direction    图标方向，0左 1上 2右 3下 默认图标位于左侧0
 */
fun TextView.setDrawable(drawable: Drawable?, iconWidth: Float? = null, iconHeight: Float? = null, direction: Int = 0) {
    if (iconWidth != null && iconHeight != null) {
        //第一个0是距左边距离，第二个0是距上边距离，iconWidth、iconHeight 分别是长宽
        drawable?.setBounds(0, 0, iconWidth.toIntPx(), iconHeight.toIntPx())
    }
    when (direction) {
        0 -> setCompoundDrawables(drawable, null, null, null)
        1 -> setCompoundDrawables(null, drawable, null, null)
        2 -> setCompoundDrawables(null, null, drawable, null)
        3 -> setCompoundDrawables(null, null, null, drawable)
        else -> throw NoSuchMethodError()
    }
}

/**
 * 设置TextView图标
 *
 * @param lDrawable     左边图标
 * @param rDrawable     右边图标
 * @param lIconWidth    图标宽dp：默认自动根据图标大小
 * @param lIconHeight   图标高dp：默认自动根据图标大小
 * @param rIconWidth    图标宽dp：默认自动根据图标大小
 * @param rIconHeight   图标高dp：默认自动根据图标大小
 */
fun TextView.setDrawables(lDrawable: Drawable?, rDrawable: Drawable?, lIconWidth: Float? = null, lIconHeight: Float? = null, rIconWidth: Float? = null, rIconHeight: Float? = null) {
    if (lIconWidth != null && lIconHeight != null) {
        lDrawable?.setBounds(0, 0, lIconWidth.toIntPx(), lIconHeight.toIntPx())
    }
    if (rIconWidth != null && rIconHeight != null) {
        rDrawable?.setBounds(0, 0, rIconWidth.toIntPx(), rIconHeight.toIntPx())
    }
    setCompoundDrawables(lDrawable, null, rDrawable, null)
}


fun EditText?.showKeyBoard(context: Context) {
    this?.let {et->
        // 设置可获得焦点
        et.isFocusable=true
        et.isFocusableInTouchMode=true
        // 获取焦点
        et.requestFocus()
        // 调用系统输入法
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

}

fun Activity.hideKeyboards() {
    // 当前焦点的 View
    val imm =
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
}

fun Activity.isServiceRunning(serviceName:String) :Boolean {
    val am = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val infos = am.getRunningServices(100)
    for (info in infos) {
        val name = info?.service?.className
        if (name == serviceName) {
            return true
        }
    }
    return false
}

/*去播放页面*/
fun Activity.toPlayAct(playStatus:Int) {
    val playIntent = Intent(this, PlayActivity::class.java)
    playIntent.putExtra(Consts.PLAY_STATUS, playStatus)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        startActivity(
            playIntent,
            ActivityOptions.makeSceneTransitionAnimation(this)
                .toBundle()
        )
    } else {
        startActivity(playIntent)
    }
}

/*去歌单页面*/
fun View?.toSongListFrg(songListType:Int) {
    if (this != null) {
        val bundle = Bundle()
        bundle.putString(Constants.SONG_LIST_TYPE,songListType.toString())
        Navigation.findNavController(this).navigate(R.id.to_song_list_fragment,bundle)
    }
}

fun View?.nav(id:Int,bundle: Bundle?=null) {
    if (this != null) {
        if (bundle!= null) {
            Navigation.findNavController(this).navigate(id,bundle)
        } else {
            Navigation.findNavController(this).navigate(id)
        }
    }
}


