package wind.maimusic.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import wind.maimusic.R

/**
 * @By Journey 2020/10/25
 * @Description
 */
abstract class BaseVpFragment : BaseFragment() {
    protected lateinit var mViewPager: ViewPager2
    protected lateinit var mTabLayout: TabLayout
    private var vpTitles: Array<String>? = null
    private var vpFragments: Array<Fragment>? = null
    private lateinit var mediator: TabLayoutMediator
    override fun initView() {
        super.initView()
        mViewPager = mRootView.findViewById(R.id.frg_main_view_pager)
        mTabLayout = mRootView.findViewById(R.id.frg_main_title_common_tab_layout)
    }

    override fun initData() {
        super.initData()
        mViewPager.adapter = VpAdapter()
        mediator = TabLayoutMediator(mTabLayout,mViewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                if (!vpTitles.isNullOrEmpty()) {
                    tab.text = vpTitles!![position]
                }
            })
        mediator.attach()
    }
    open fun initVpTitle(title: Array<String>?) {
        this.vpTitles = title
    }

    open fun initVpFragments(fragments: Array<Fragment>?) {
        this.vpFragments = fragments
    }

    inner class VpAdapter : FragmentStateAdapter(this) {
        override fun getItemCount() = if (vpFragments == null) 0 else vpFragments!!.size
        override fun createFragment(position: Int) = vpFragments!![position]
    }

    override fun onDestroy() {
        super.onDestroy()
        mediator.detach()
    }
}