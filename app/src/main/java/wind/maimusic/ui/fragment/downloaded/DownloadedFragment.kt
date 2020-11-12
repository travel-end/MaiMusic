package wind.maimusic.ui.fragment.downloaded

import androidx.lifecycle.Observer
import wind.maimusic.R
import wind.maimusic.base.BaseSongListFragment
import wind.maimusic.utils.visible
import wind.maimusic.vm.DownloadedViewModel
import wind.widget.cost.Consts
import wind.widget.effcientrv.submitList

class DownloadedFragment : BaseSongListFragment<DownloadedViewModel>() {
    companion object {
        fun newInstance(): DownloadedFragment {
            return DownloadedFragment()
        }
    }

    override fun layoutResId() = R.layout.fragment_downloaded
    override fun songListType() = Consts.LIST_TYPE_DOWNLOAD
    override fun initView() {
        super.initView()
        intiPlayAll(false)
        mViewModel.getDownloadedSongs()
    }

    override fun observe() {
        super.observe()
        mViewModel.downloadedSong.observe(this, Observer {
            downloadedSongs.addAll(it)
            rvSongList.submitList(downloadedSongs)
            rvSongList.visible()
            flPlayAll.visible()
        })
    }

    override fun initStatusBar() {

    }
}