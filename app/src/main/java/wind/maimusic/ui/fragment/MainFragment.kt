package wind.maimusic.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseVpFragment
import wind.maimusic.model.MaiTab
import wind.maimusic.utils.getStringRes
import wind.widget.tablayout.CustomTab
import wind.widget.utils.fastClickListener

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
    override fun initAction() {
        super.initAction()
        mTitleSearch.fastClickListener {
            val bundle = Bundle()
            bundle.putString(Constants.HOT_SEARCH,"昨夜雨疏风骤")
            Navigation
                .findNavController(it)
                .navigate(R.id.action_mainFragment_to_searchFragment,bundle)
        }
    }
}