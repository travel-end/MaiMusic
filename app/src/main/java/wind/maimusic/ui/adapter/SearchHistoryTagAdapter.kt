package wind.maimusic.ui.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import wind.widget.taglayout.TagBean
import wind.widget.taglayout.adapter.TagAdapter
import wind.widget.taglayout.tags.DefaultTagView

/**
 * @By Journey 2020/10/27
 * @Description
 */
class SearchHistoryTagAdapter constructor(private val activity:Activity):TagAdapter<TagBean>() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val tagView = DefaultTagView(activity)
        val item = getItem(position) as TagBean
        tagView.text = item.name
        return tagView
    }
}