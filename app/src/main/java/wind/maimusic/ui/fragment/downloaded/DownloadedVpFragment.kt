package wind.maimusic.ui.fragment.downloaded

import android.widget.TextView
import androidx.fragment.app.Fragment
import wind.maimusic.R
import wind.maimusic.base.BaseVpFragment
import wind.maimusic.model.MaiTab
import wind.maimusic.utils.getStringRes
import wind.widget.tablayout.CustomTab

/**
 * @By Journey 2020/11/2
 * @Description
 */
class DownloadedVpFragment : BaseVpFragment() {
    override val vpFragments: Array<Fragment>
        get() = arrayOf(
            DownloadedFragment.newInstance(),
            DownloadingFragment.newInstance()
        )

    override fun layoutResId()=R.layout.fragment_download_vp

    override val vpTabTitles = ArrayList<CustomTab>().apply {
        add(MaiTab(R.string.has_downloaded.getStringRes()))
        add(MaiTab(R.string.downloading.getStringRes()))
    }
    override fun initView() {
        mViewPager = mRootView.findViewById(R.id.frg_main_view_pager)
        mTabLayout =  mRootView.findViewById(R.id.frg_main_title_common_tab_layout)
        mRootView.findViewById<TextView>(R.id.title_tv).text = R.string.download_music.getStringRes()
    }
}