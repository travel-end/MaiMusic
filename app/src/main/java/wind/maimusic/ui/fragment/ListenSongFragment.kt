package wind.maimusic.ui.fragment

import androidx.fragment.app.Fragment
import com.zinc.jrecycleview.JRecycleView
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.vm.ListenSongViewModel

/**
 * 听歌tab
 * 页面数据类型分析
 * 一楼banner、 图片+文字（可变项）
 * 二楼5个tab（固定项）
 * 三楼标题：精选歌单
 * 四楼歌单推荐（可变向）
 * 五楼标题：单曲推荐
 * 六楼推荐单曲
 * 七楼标题：诗＆歌
 * 八楼诗＆歌推荐
 */
class ListenSongFragment:BaseLifeCycleFragment<ListenSongViewModel> (){
    private lateinit var rvListenSong:JRecycleView
    override fun layoutResId()=R.layout.fragment_listen_song
    companion object {
        fun newInstance() : Fragment {
            return ListenSongFragment()
        }
    }

    override fun initView() {
        super.initView()
        rvListenSong = mRootView.findViewById(R.id.rv_listen)
    }

    override fun initData() {
        super.initData()
        mViewModel.getListenData()
    }

    override fun observe() {
        super.observe()

    }
}