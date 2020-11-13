package wind.maimusic.ui.adapter

import android.view.View
import wind.maimusic.model.core.AlListBean

/**
 * @By Journey 2020/11/13
 * @Description
 */
interface OnAlbumItemClickListener {
    fun onAlbumItemClick(album: AlListBean, position:Int,view:View)
}