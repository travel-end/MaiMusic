package wind.maimusic.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.Gravity
import wind.maimusic.R
import wind.maimusic.utils.LogUtil

abstract class BaseDialog(context:Context):Dialog(context,R.style.dialog_style) {
    private var widthProportion:Float = 0.8f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId())
        isCancelable()
        initView()
        initData()
    }
    abstract fun layoutResId():Int
    open fun isCancelable(cancelable:Boolean = false) {
        setCancelable(cancelable)
        setCanceledOnTouchOutside(cancelable)
    }
    open fun initView() {

    }
    open fun initData() {

    }


    override fun onStart() {
        super.onStart()
        val win = window
        val lp = win?.attributes
//        win?.setBackgroundDrawableResource(android.R.color.transparent)
        if (lp != null) {
            lp.gravity = Gravity.CENTER
            val dialogWidth =(Resources.getSystem().displayMetrics.widthPixels) * 0.8
            LogUtil.e("dialogWidth:$dialogWidth")
            lp.width = dialogWidth.toInt()//* widthProportion.toInt()
            win.attributes = lp
        }
    }

    open fun widthProportion(width:Float) {
        this.widthProportion = width
    }
}