package wind.maimusic.ui.fragment.local

import androidx.lifecycle.Observer
import wind.maimusic.R
import wind.maimusic.base.BaseSongListFragment
import wind.maimusic.utils.*
import wind.maimusic.vm.LocalSongViewModel
import wind.widget.cost.Consts
import wind.widget.effcientrv.*
import wind.widget.utils.fastClickListener

class LocalSongFragment:BaseSongListFragment<LocalSongViewModel>() {
    override fun layoutResId()= R.layout.fragment_local
    override fun initView() {
        super.initView()
        initTitle(R.string.local_music.getStringRes(),R.string.scan_music.getStringRes())
        intiPlayAll(false)
        val song = SongUtil.getSong()
        if (song != null) {
            layoutManager.scrollToPositionWithOffset(
                song.position - 4,rvSongList.height
            )
        }
    }

    override fun songListType()=Consts.LIST_TYPE_LOCAL
    override fun initData() {
        super.initData()
        /*获取数据库中本地音乐列表*/
        mViewModel.getLocalSong()
        tvFunc?.fastClickListener {
            mViewModel.scanLocalMusic()
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.dbLocalSong.observe(this,Observer{
                if (isNotNullOrEmpty(it)) {
                    localSongs.clear()
                    localSongs.addAll(it)
                    rvSongList.submitList(localSongs)
                    rvSongList.visible()
                    flPlayAll.visible()
                } else {
                    rvSongList.gone()
                    flPlayAll.gone()
                }
        })
        mViewModel.scanMusic.observe(this,Observer{
            if (isNotNullOrEmpty(it)) {
                hideLoadingResultView()
                "发现了您手机上的${it.size}首音乐".toast()
                localSongs.clear()
                localSongs.addAll(it)
                rvSongList.submitList(localSongs)
                rvSongList.visible()
                flPlayAll.visible()
            } else {
                R.string.no_local_music.getStringRes().toast()
                rvSongList.gone()
                flPlayAll.gone()
            }
        })
    }

    override fun initStatusBar() {

    }
}