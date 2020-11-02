package wind.maimusic.ui.fragment.downloaded

import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.vm.DownloadingViewModel

/**
 * @By Journey 2020/11/2
 * @Description 下载中的音乐队列
 */
class DownloadingFragment:BaseLifeCycleFragment<DownloadingViewModel>() {
    companion object {
        fun newInstance():DownloadingFragment{
            return DownloadingFragment()
        }
    }
    override fun layoutResId()=R.layout.fragment_downloading
}