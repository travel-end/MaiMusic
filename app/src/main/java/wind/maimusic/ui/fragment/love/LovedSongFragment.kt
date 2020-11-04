package wind.maimusic.ui.fragment.love

import androidx.lifecycle.Observer
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseSongListFragment
import wind.maimusic.model.songlist.SongListTop
import wind.maimusic.utils.isNotNullOrEmpty
import wind.maimusic.utils.visible
import wind.maimusic.vm.LoveSongViewModel
import wind.widget.cost.Consts
import wind.widget.effcientrv.submitList

class LovedSongFragment : BaseSongListFragment<LoveSongViewModel>() {
    override fun layoutResId() = R.layout.fragment_md_style_song_list
    override fun songListType() = Consts.LIST_TYPE_LOVE
    override fun initSongListInfo() {
        super.initSongListInfo()
        songListTop = SongListTop(
            "我喜欢的音乐",
            "我喜欢你，像风走了八千里，不问归期",
            "By Journey - Travel end -",
            Constants.TEMP_SONG_COVER1_NORMAL,
            "喜欢",
            "清凉"
        )
    }
    override fun initData() {
        super.initData()
        mViewModel.findAllLovedSongs()
    }

    override fun observe() {
        super.observe()
        mViewModel.lovedSongs.observe(this,Observer{
            if (isNotNullOrEmpty(it)) {
                rvSongList.submitList(it!!)
                rvSongList.visible()
                flPlayAll.visible()
            }
        })
    }
}