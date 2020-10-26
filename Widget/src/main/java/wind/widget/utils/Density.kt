package wind.widget.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

/**
 * @By Journey 2020/10/26
 * @Description
 */

fun Float.toIntPx():Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    ).toInt()
}

fun Float.toFloatPx():Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )
}

fun getScreenWidth(context: Context?):Int {
    if (context == null) {
        return 0
    }
    return context.resources.displayMetrics.widthPixels
}

fun getScreenHeight(context: Context?):Int {
    if (context == null) {
        return 0
    }
    return context.resources.displayMetrics.heightPixels
}