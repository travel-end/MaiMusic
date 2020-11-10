package wind.maimusic.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import wind.maimusic.R

/**
 * @By Journey 2020/10/25
 * @Description
 */
abstract class BaseVpFragment : BaseFragment() {
    private var mPageChangeCallback: PageChangeCallback? = null
    protected lateinit var mViewPager: ViewPager2
    protected lateinit var mTabLayout: TabLayout
    private var vpTitles: Array<TabLayout.Tab>? = null
    private var vpFragments: Array<Fragment>? = null

    override fun initView() {
        mViewPager = mRootView.findViewById(R.id.frg_main_view_pager)
        mTabLayout = mRootView.findViewById(R.id.frg_main_title_common_tab_layout)
    }

    override fun initData() {
        super.initData()
        mViewPager.adapter = VpAdapter()
        mTabLayout.run {
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        mViewPager.currentItem = tab.position
                    }
                }

            })
            if (!vpTitles.isNullOrEmpty()) {
                repeat(vpTitles!!.size) {
                    addTab(vpTitles!![it])
                }
            }

        }
        mPageChangeCallback = PageChangeCallback()
        mViewPager.registerOnPageChangeCallback(mPageChangeCallback!!)
    }
    open fun initVpTitle(title: Array<TabLayout.Tab>?) {
        this.vpTitles = title
    }

    open fun initVpFragments(fragments: Array<Fragment>?) {
        this.vpFragments = fragments
    }

    inner class VpAdapter : FragmentStateAdapter(this) {
        override fun getItemCount() = if (vpFragments == null) 0 else vpFragments!!.size
        override fun createFragment(position: Int) = vpFragments!![position]
    }

    inner class PageChangeCallback : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            mTabLayout.setScrollPosition(position, 0f, true, true)
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