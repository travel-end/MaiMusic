package wind.maimusic.ui.fragment.songlist

import androidx.lifecycle.Observer
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseSongListFragment
import wind.maimusic.model.OnlineSong
import wind.maimusic.model.songlist.SongListTop
import wind.maimusic.utils.isNotNullOrEmpty
import wind.maimusic.utils.visible
import wind.maimusic.vm.SongListViewModel
import wind.widget.effcientrv.submitList

/**
 * @By Journey 2020/11/2
 * @Description 歌单列表
 */
class SongListFragment:BaseSongListFragment<SongListViewModel>() {
    private var listType :Int = 0
    override fun songListType()=listType

    override fun initView() {
        super.initView()
        listType = arguments?.getString(Constants.SONG_LIST_TYPE)?.toInt()?:0
    }

    override fun layoutResId()= R.layout.fragment_md_style_song_list

    override fun initData() {
        if (listType != 0) {
            mViewModel.getOnlineSongs(listType)
        }
        super.initData()
    }

    override fun observe() {
        super.observe()
        mViewModel.onlineSongs.observe(this,Observer{
            if (isNotNullOrEmpty(it)) {
                onlineSongs.clear()
                onlineSongs.addAll(it)
                initSongListInfo(getSongListTop(Constants.ST_DAILY_RECOMMEND,onlineSongs[0]))
                rvSongList.submitList(onlineSongs)
                rvSongList.visible()
                flPlayAll.visible()
            }
        })
    }

    private fun getSongListTop(type:Int,onlineSong: OnlineSong):SongListTop? {
        var onlineSongTop:SongListTop?=null
        when(type) {
            Constants.ST_DAILY_RECOMMEND->{
                 onlineSongTop = SongListTop(
                    "每日推荐",
                    "入我相思门，知我相思苦。\n 早知如此绊人心，何如当初莫相识",
                    "By suo luo -",
                    onlineSong.imgUrl,
                    "推荐",
                    "清新"
                )
            }
        }
        return onlineSongTop
    }

}