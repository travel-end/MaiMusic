package wind.maimusic.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import wind.maimusic.R
import wind.maimusic.utils.isNotNullOrEmpty

/**
 * @By Journey 2020/11/5
 * @Description
 */
class FloatDialog(context: Context,private val msg: String?=null):Dialog(context,R.style.dialog_style) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_loading_normal)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        val tvMsg = findViewById<TextView>(R.id.loading_view_tv_message)
        if (msg.isNotNullOrEmpty()) {
            tvMsg?.text = msg
        }
    }
}