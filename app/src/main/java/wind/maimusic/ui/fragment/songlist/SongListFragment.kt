package wind.maimusic.ui.fragment.songlist

import androidx.lifecycle.Observer
import com.gyf.immersionbar.ImmersionBar
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
 * @Description 歌单列表 在线音乐
 */
class SongListFragment : BaseSongListFragment<SongListViewModel>() {
    private var listType: Int = 0
    override fun songListType() = listType
    override fun initView() {
        listType = arguments?.getString(Constants.SONG_LIST_TYPE)?.toInt() ?: 0
        super.initView()
    }
    override fun layoutResId() = R.layout.fragment_md_style_song_list
    override fun initData() {
        if (listType != 0) {
            mViewModel.getOnlineSongs(listType)
        }
        super.initData()
    }

    override fun observe() {
        super.observe()
        mViewModel.onlineSongs.observe(this, Observer {
            if (isNotNullOrEmpty(it)) {
                onlineSongs.clear()
                onlineSongs.addAll(it)
                rvSongList.submitList(onlineSongs)
                rvSongList.visible()
                flPlayAll.visible()
            }
            setSongListTop(Constants.ST_DAILY_RECOMMEND)// 每日推荐
        })
    }
}