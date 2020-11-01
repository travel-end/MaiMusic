package wind.maimusic.ui.fragment.local

import androidx.lifecycle.Observer
import wind.maimusic.R
import wind.maimusic.base.BaseSongListFragment
import wind.maimusic.vm.LocalSongViewModel

class LocalSongFragment:BaseSongListFragment<LocalSongViewModel>() {
    override fun layoutResId()= R.layout.fragment_local
    override fun initData() {
        super.initData()
        mViewModel.getLocalSong()
    }

    override fun observe() {
        super.observe()
        mViewModel.dbLocalSong.observe(this,Observer{
            it?.let {

            }
        })
        mViewModel.scanMusic.observe(this,Observer{
            it?.let {

            }
        })

    }

    override fun doFunc() {
        mViewModel.scanLocalMusic()
    }
}