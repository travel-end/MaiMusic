package wind.maimusic.ui.fragment

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseFragment
import wind.maimusic.utils.LogUtil
import wind.maimusic.utils.gone
import wind.maimusic.utils.visible
import wind.widget.tablayout.OnTabSelectListener
import wind.widget.utils.fastClickListener

class MainFragment : BaseFragment(){
    private lateinit var tabLayout:TabLayout
    private lateinit var viewPager:ViewPager2
    private lateinit var searchView:LinearLayout
    private lateinit var tabOneIcon:ImageView
    private lateinit var tabSecondIcon:ImageView
    private lateinit var tabThirdIcon:ImageView
    private var pageChangeCallback: MainPageChangeCallback? = null
    private val fragments = arrayOf(ListenSongFragment.newInstance(),FindFragment.newInstance(),FavoritesFragment.newInstance())

    override fun layoutResId()=R.layout.fragment_main

    override fun initView() {
        super.initView()
        tabLayout = mRootView.findViewById(R.id.main_title_tab_layout)
        viewPager = mRootView.findViewById(R.id.frg_main_view_pager)
        searchView = mRootView.findViewById(R.id.main_title_search)
        tabOneIcon = mRootView.findViewById(R.id.main_title_tab_one_icon)
        tabSecondIcon = mRootView.findViewById(R.id.main_title_tab_two_icon)
        tabThirdIcon = mRootView.findViewById(R.id.main_title_tab_third_icon)
        viewPager.offscreenPageLimit=1
        viewPager.adapter = MainVpAdapter()
        pageChangeCallback = MainPageChangeCallback()
        viewPager.registerOnPageChangeCallback(pageChangeCallback!!)
    }
    override fun initAction() {
        super.initAction()
        searchView.fastClickListener {
            val bundle = Bundle()
            // TODO: 2020/11/4 这里的专场动画可以优化
            bundle.putString(Constants.HOT_SEARCH,"昨夜雨疏风骤，浓睡不消残酒")
            Navigation
                .findNavController(it)
                .navigate(R.id.to_search_main_fragment,bundle)
        }
        tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                viewPager.currentItem = position?:0
                setTabIconVisible(position?:0)
            }
        })
    }

    inner class MainVpAdapter:FragmentStateAdapter(this) {
        override fun getItemCount()=fragments.size
        override fun createFragment(position: Int)=fragments[position]

    }
    inner class MainPageChangeCallback : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
//            LogUtil.e("po:$position")
            tabLayout.setScrollPosition(position,0f,true,true)
            setTabIconVisible(position)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        pageChangeCallback?.let {
            viewPager.unregisterOnPageChangeCallback(it)
        }
        pageChangeCallback = null
    }

    fun setTabIconVisible(position:Int) {
        when(position) {
            0->{
                tabOneIcon.visible()
                tabSecondIcon.gone()
                tabThirdIcon.gone()
            }
            1->{
                tabOneIcon.gone()
                tabSecondIcon.visible()
                tabThirdIcon.gone()
            }
            2->{
                tabOneIcon.gone()
                tabSecondIcon.gone()
                tabThirdIcon.visible()
            }
        }
    }

}