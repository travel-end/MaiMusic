package wind.maimusic.ui.fragment.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseVpFragment
import wind.maimusic.model.MaiTab
import wind.maimusic.utils.getStringRes
import wind.widget.tablayout.CustomTab

class SearchVpFragment:BaseVpFragment() {
    companion object {
        fun newInstance(searchContent:String) :SearchVpFragment{
            val fragment = SearchVpFragment()
            fragment.arguments = Bundle().apply {
                putString(Constants.KEY_SEARCH_CONTENT,searchContent)
            }
            return fragment
        }
    }
    override fun layoutResId()= R.layout.fragment_search_vp
    override fun initView() {
        mViewPager = mRootView.findViewById(R.id.frg_main_view_pager)
        mTabLayout =  mRootView.findViewById(R.id.frg_main_title_common_tab_layout)
    }
    override val vpFragments: Array<Fragment>
        get() = arrayOf(
            SearchSingleSongFragment.newInstance(),
            SearchAlbumFragment.newInstance()
        )
    override val vpTabTitles = ArrayList<CustomTab>().apply {
        add(MaiTab(R.string.single_song.getStringRes()))
        add(MaiTab(R.string.mv.getStringRes()))
    }
}