package wind.maimusic.ui.adapter

import android.view.View

interface OnLsItemClickListener<T> {
    fun onItemClick(type:Int,t:T,position:Int)
    fun onHoldItemClick(view: View, t:T)
}