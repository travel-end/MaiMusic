package wind.maimusic.ui.adapter

import android.view.View
import wind.maimusic.model.core.ListBean

/**
 * @By Journey 2020/11/13
 * @Description
 */
interface OnSingleSongItemClickListener {
    fun onSingleSongItemClick(song:ListBean,position:Int)
    fun onRightFunctionClick(view:View)
}