package wind.maimusic.base

import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import wind.maimusic.R
import wind.widget.tablayout.CommonTabLayout
import wind.widget.tablayout.CustomTab
import wind.widget.tablayout.OnTabSelectListener

/**
 * @By Journey 2020/10/25
 * @Description
 */
abstract class BaseVpFragment : BaseFragment() {
    private var mPageChangeCallback: PageChangeCallback? = null
    protected lateinit var mViewPager: ViewPager2
    protected lateinit var mTitleList: ImageView
    protected lateinit var mTitleSearch: ImageView
    protected lateinit var mTabLayout: CommonTabLayout

    /*2020/1104 使用tabLayout与viewPager搭配*/
//    protected lateinit var mTb:TabLayout

    override fun initView() {
        mViewPager = mRootView.findViewById(R.id.frg_main_view_pager)
        mTitleList = mRootView.findViewById(R.id.frg_main_title_iv_list)
        mTitleSearch = mRootView.findViewById(R.id.frg_main_title_iv_search)
        mTabLayout = mRootView.findViewById(R.id.frg_main_title_common_tab_layout)
    }

    override fun initData() {
        super.initData()
        mViewPager.run {
            offscreenPageLimit = 1
            adapter = vpAdapter
        }
        mTabLayout.run {
            setTabData(vpTabTitles)
            setOnTabSelectListener(object : OnTabSelectListener {
                override fun onTabSelect(position: Int) {
                    mViewPager.currentItem = position
                }

                override fun onTabReselect(position: Int) {
                }
            })
        }
        mPageChangeCallback = PageChangeCallback()
        mViewPager.registerOnPageChangeCallback(mPageChangeCallback!!)
    }

    abstract val vpFragments: Array<Fragment>

    abstract val vpTabTitles: ArrayList<CustomTab>

    private val vpAdapter: VpAdapter by lazy {
        VpAdapter(requireActivity()).apply {
            addFragment(vpFragments)
        }
    }

    inner class VpAdapter(act: FragmentActivity) : FragmentStateAdapter(act) {
        private val fragments = mutableListOf<Fragment>()
        fun addFragment(frg: Array<Fragment>) {
            fragments.addAll(frg)
        }

        override fun getItemCount() = fragments.size
        override fun createFragment(position: Int) = fragments[position]
    }

    inner class PageChangeCallback : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            mTabLayout.currentTab = position
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPageChangeCallback?.let {
            mViewPager.unregisterOnPageChangeCallback(it)
        }
        mPageChangeCallback = null
    }
}