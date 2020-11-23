package wind.maimusic.ui.fragment.songlist

import android.os.Bundle
import androidx.lifecycle.Observer
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseSongListFragment
import wind.maimusic.utils.isNotNullOrEmpty
import wind.maimusic.utils.visible
import wind.maimusic.vm.CreatedSongListViewModel
import wind.widget.cost.Consts
import wind.widget.effcientrv.submitList

/**
 * @By Journey 2020/11/20
 * @Description
 */
class CreatedSongListFragment:BaseSongListFragment<CreatedSongListViewModel>() {
    private var listId:Int=0
    private var songListName:String = ""
    private var songListCover:String = ""
    override fun songListType()=Consts.LIST_TYPE_MY_CREATED
    override fun layoutResId()= R.layout.fragment_md_style_song_list

    override fun initData() {
        if (listId != 0) {
            mViewModel.getSongListById(listId)
        }
        super.initData()
    }

    override fun initView() {
        super.initView()
        setSongListTop(songListName,songListCover)
    }
    override fun observe() {
        super.observe()
        mViewModel.createSongs.observe(this,Observer{
            if (isNotNullOrEmpty(it)) {
                onlineSongs.clear()
                onlineSongs.addAll(it)
                rvSongList.submitList(onlineSongs)
                rvSongList.visible()
                flPlayAll.visible()
            }
        })
    }
    override fun getBundle(bundle: Bundle) {
        listId = bundle.getInt(Constants.SONG_LIST_ID)
        songListName = bundle.getString(Constants.SONG_LIST_NAME,"")
        songListCover = bundle.getString(Constants.SONG_LIST_COVER,"")
    }
}