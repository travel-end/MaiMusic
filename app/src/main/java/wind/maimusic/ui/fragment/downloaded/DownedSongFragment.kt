package wind.maimusic.ui.fragment.downloaded

import wind.maimusic.R
import wind.maimusic.base.BaseSongListFragment
import wind.maimusic.vm.DownloadedViewModel
import wind.widget.cost.Consts

class DownedSongFragment:BaseSongListFragment<DownloadedViewModel>() {
    override fun layoutResId()= R.layout.fragment_downloaded
    override fun songListType()=Consts.LIST_TYPE_DOWNLOAD
}