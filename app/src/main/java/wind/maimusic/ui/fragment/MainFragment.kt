package wind.maimusic.ui.fragment

import androidx.fragment.app.Fragment
import wind.maimusic.R
import wind.maimusic.base.BaseVpFragment
import wind.maimusic.model.MaiTab
import wind.maimusic.utils.getStringRes
import wind.widget.tablayout.CustomTab

class MainFragment :BaseVpFragment(){
    override val vpFragments: Array<Fragment>
        get() = arrayOf(
            ListenSongFragment.newInstance(),
            FavoritesFragment.newInstance()
        )
    override val vpTabTitles = ArrayList<CustomTab>().apply {
        add(MaiTab(R.string.tab_listener_song.getStringRes()))
        add(MaiTab(R.string.tab_my_save.getStringRes()))
    }

    override fun layoutResId()=R.layout.fragment_main
    override fun initData() {
        super.initData()
//        mViewPager.setCurrentItem(1,false)
    }
}