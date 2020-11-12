package wind.maimusic.ui.fragment.history

import wind.maimusic.R
import wind.maimusic.base.BaseSongListFragment
import wind.maimusic.vm.RecentReadViewModel
import wind.widget.cost.Consts

class RecentReadFragment:BaseSongListFragment<RecentReadViewModel>() {
    override fun layoutResId()=R.layout.fragment_recent_read
    override fun songListType()=Consts.POETRY_TYPE_RECENT_READ
    override fun initStatusBar() {
    }
}