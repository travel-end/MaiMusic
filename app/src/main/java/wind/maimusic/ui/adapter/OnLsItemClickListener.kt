package wind.maimusic.ui.adapter

interface OnLsItemClickListener<T> {
    fun onItemClick(type:Int,t:T,position:Int)
    fun onHoldItemClick(t:T)
}