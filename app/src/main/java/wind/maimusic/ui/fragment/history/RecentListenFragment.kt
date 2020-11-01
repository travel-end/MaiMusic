package wind.maimusic.ui.fragment.history

import wind.maimusic.R
import wind.maimusic.base.BaseSongListFragment
import wind.maimusic.vm.RecentListenViewModel

class RecentListenFragment:BaseSongListFragment<RecentListenViewModel>() {
    override fun layoutResId()=R.layout.fragment_recent_listener
}