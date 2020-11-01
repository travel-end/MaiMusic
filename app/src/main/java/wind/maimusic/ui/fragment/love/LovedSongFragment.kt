package wind.maimusic.ui.fragment.love

import wind.maimusic.R
import wind.maimusic.base.BaseSongListFragment
import wind.maimusic.vm.LoveSongViewModel
import wind.widget.cost.Consts

class LovedSongFragment :BaseSongListFragment<LoveSongViewModel>(){
    override fun layoutResId()=R.layout.fragment_loved_song
    override fun songListType()=Consts.LIST_TYPE_LOVE
}