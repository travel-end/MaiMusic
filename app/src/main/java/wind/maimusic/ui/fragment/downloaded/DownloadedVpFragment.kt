package wind.maimusic.ui.fragment.downloaded

import android.widget.TextView
import wind.maimusic.R
import wind.maimusic.base.BaseVpFragment
import wind.maimusic.utils.getStringRes

/**
 * @By Journey 2020/11/2
 * @Description
 */
class DownloadedVpFragment : BaseVpFragment() {
    override fun layoutResId() = R.layout.fragment_download_vp
    override fun initView() {
        super.initView()
        mRootView.findViewById<TextView>(R.id.title_tv).text =
            R.string.download_music.getStringRes()
        initVpTitle(
            arrayOf(
                mTabLayout.newTab().setText(R.string.has_downloaded.getStringRes())
                , mTabLayout.newTab().setText(R.string.downloading.getStringRes())
            )
        )
        initVpFragments(
            arrayOf(
                DownloadedFragment.newInstance(),
                DownloadingFragment.newInstance()
            )
        )
    }
}