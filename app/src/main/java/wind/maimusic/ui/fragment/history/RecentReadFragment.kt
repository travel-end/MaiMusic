package wind.maimusic.ui.fragment.history

import wind.maimusic.R
import wind.maimusic.base.BaseSongListFragment
import wind.maimusic.vm.RecentReadViewModel

class RecentReadFragment:BaseSongListFragment<RecentReadViewModel>() {
    override fun layoutResId()=R.layout.fragment_recent_read
}