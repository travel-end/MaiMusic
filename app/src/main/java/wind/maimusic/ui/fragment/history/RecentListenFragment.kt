package wind.maimusic.ui.fragment.history

import androidx.lifecycle.Observer
import wind.maimusic.R
import wind.maimusic.base.BaseSongListFragment
import wind.maimusic.utils.getStringRes
import wind.maimusic.utils.isNotNullOrEmpty
import wind.maimusic.utils.visible
import wind.maimusic.vm.RecentListenViewModel
import wind.widget.cost.Consts
import wind.widget.effcientrv.submitList

class RecentListenFragment:BaseSongListFragment<RecentListenViewModel>() {
    override fun layoutResId()=R.layout.fragment_recent_listener
    override fun initView() {
        super.initView()
        initTitle(R.string.recent_play.getStringRes(),R.string.clear_all.getStringRes())
    }

    override fun initData() {
        super.initData()
        mViewModel.findAllRecentListenSongs()
    }

    override fun observe() {
        super.observe()
        mViewModel.recentListenSongs.observe(this,Observer{
            if (isNotNullOrEmpty(it)) {
                recentListenSongs.clear()
                recentListenSongs.addAll(it)
                rvSongList.submitList(recentListenSongs)
                rvSongList.visible()
                flPlayAll.visible()
            }
        })
    }

    override fun songListType()=Consts.LIST_TYPE_HISTORY
}