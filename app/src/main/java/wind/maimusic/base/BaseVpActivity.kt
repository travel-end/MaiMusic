//package wind.maimusic.base
//
//import android.widget.ImageView
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentActivity
//import androidx.viewpager2.adapter.FragmentStateAdapter
//import androidx.viewpager2.widget.ViewPager2
//import wind.maimusic.R
//import wind.widget.tablayout.CommonTabLayout
//import wind.widget.tablayout.CustomTab
//import wind.widget.tablayout.OnTabSelectListener
//
///**
// * @By Journey 2020/10/25
// * @Description
// */
//abstract class BaseVpActivity:BaseActivity() {
//    private var mPageChangeCallback: PageChangeCallback? = null
//    protected lateinit var mViewPager: ViewPager2
//    protected lateinit var mTitleList: ImageView
//    protected lateinit var mTitleSearch: ImageView
//    protected lateinit var mTabLayout: CommonTabLayout
//    override fun initView() {
//        mViewPager = findViewById(R.id.frg_main_view_pager)
//        mTitleList = findViewById(R.id.frg_main_title_iv_list)
//        mTitleSearch = findViewById(R.id.frg_main_title_iv_search)
//        mTabLayout = findViewById(R.id.frg_main_title_common_tab_layout)
//    }
//
//    override fun initData() {
//        super.initData()
//        mViewPager.run {
//            offscreenPageLimit = 1
//            adapter = vpAdapter
//        }
//        mTabLayout.run {
//            setTabData(vpTabTitles)
//            setOnTabSelectListener(object : OnTabSelectListener {
//                override fun onTabSelect(position: Int) {
//                    mViewPager.currentItem = position
//                }
//
//                override fun onTabReselect(position: Int) {
//                }
//            })
//        }
//        mPageChangeCallback = PageChangeCallback()
//        mViewPager.registerOnPageChangeCallback(mPageChangeCallback!!)
//    }
//
//    abstract val vpFragments: Array<Fragment>
//
//    abstract val vpTabTitles: ArrayList<CustomTab>
//
//    private val vpAdapter: VpAdapter by lazy {
//        VpAdapter(this).apply {
//            addFragment(vpFragments)
//        }
//    }
//
//    inner class VpAdapter(act: FragmentActivity) : FragmentStateAdapter(act) {
//        private val fragments = mutableListOf<Fragment>()
//        fun addFragment(frg: Array<Fragment>) {
//            fragments.addAll(frg)
//        }
//
//        override fun getItemCount() = fragments.size
//        override fun createFragment(position: Int) = fragments[position]
//    }
//
//    inner class PageChangeCallback : ViewPager2.OnPageChangeCallback() {
//        override fun onPageSelected(position: Int) {
//            super.onPageSelected(position)
//            mTabLayout.currentTab = position
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        mPageChangeCallback?.let {
//            mViewPager.unregisterOnPageChangeCallback(it)
//        }
//        mPageChangeCallback = null
//    }
//}