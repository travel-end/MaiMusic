package wind.maimusic.widget.dialog

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import wind.maimusic.R
import wind.maimusic.utils.LogUtil

class BottomFunctionDialog(context: Context):BaseDialog(context) {
    override fun layoutResId()=R.layout.view_bottom_function

    override fun initView() {
        super.initView()
    }

    override fun isCancelable(cancelable: Boolean) {
        super.isCancelable(true)
    }
    override fun onStart() {
        val win = window
        val lp = win?.attributes
        win?.setBackgroundDrawableResource(R.drawable.shape_white_rect_bg)
        win?.setWindowAnimations(R.style.bottomAnimation)
        if (lp != null) {
            lp.gravity = Gravity.BOTTOM
            val dialogWidth =(Resources.getSystem().displayMetrics.widthPixels) *1.0
            LogUtil.e("dialogWidth:$dialogWidth")
            lp.width = dialogWidth.toInt()//* widthProportion.toInt()
            win.attributes = lp
        }
    }
}