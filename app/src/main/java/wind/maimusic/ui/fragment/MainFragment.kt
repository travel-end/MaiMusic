package wind.maimusic.ui.fragment

import android.widget.ImageView
import android.widget.LinearLayout
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayout
import wind.maimusic.R
import wind.maimusic.base.BaseVpFragment
import wind.maimusic.ui.activities.MainActivity
import wind.maimusic.utils.getStringRes
import wind.maimusic.utils.gone
import wind.maimusic.utils.visible
import wind.widget.utils.fastClickListener

class MainFragment : BaseVpFragment() {
    private lateinit var searchView: LinearLayout
    private lateinit var tabOneIcon: ImageView
    private lateinit var tabSecondIcon: ImageView
    private lateinit var tabThirdIcon: ImageView
    private lateinit var ivDrawerList: ImageView
    override fun layoutResId() = R.layout.fragment_main
    override fun initView() {
        super.initView()
        searchView = mRootView.findViewById(R.id.main_title_search)
        tabOneIcon = mRootView.findViewById(R.id.main_title_tab_one_icon)
        tabSecondIcon = mRootView.findViewById(R.id.main_title_tab_two_icon)
        tabThirdIcon = mRootView.findViewById(R.id.main_title_tab_third_icon)
        ivDrawerList = mRootView.findViewById(R.id.main_title_list)
        /**
         * ViewPager2的offScreenPageLimit默认值为OFFSCREEN_PAGE_LIMIT_DEFAULT,
         * 当setOffscreenPageLimit为OFFSCREEN_PAGE_LIMIT_DEFAULT时候会使用RecyclerView的缓存机制。
         * 默认只会加载当前显示的Fragment,而不会像ViewPager一样至少预加载一个item.当切换到下一个item的时候，
         * 当前Fragment会执行onPause方法，而下一个Fragment则会从onCreate一直执行到onResume。
         * 当再次滑动回第一个页面的时候当前页面同样会执行onPuase，而第一个页面会执行onResume。
         * 也就是说：ViewPager2中，默认关闭了预加载机制。
         * 网络请求放到onStart中即可。(使用ViewPager2关闭limit就可以不用考虑延迟加载的问题)
         */
        mViewPager.offscreenPageLimit = 1// 将我的页面提前加载好
//        viewPager.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
//        viewPager.adapter = MainVpAdapter()
//        pageChangeCallback = MainPageChangeCallback()
//        viewPager.registerOnPageChangeCallback(pageChangeCallback!!)
        initVpTitle(
            arrayOf(
                R.string.tab_listener_song.getStringRes(),
                R.string.tab_my_save.getStringRes(),
                R.string.tab_discover.getStringRes()
            )
        )
        initVpFragments(
            arrayOf(
                ListenSongFragment.newInstance(),
                FavoritesFragment.newInstance(),
                FindFragment.newInstance()
            )
        )
    }

    override fun initAction() {
        super.initAction()
        searchView.fastClickListener {
            Navigation
                .findNavController(it)
                .navigate(R.id.to_search_main_fragment)
        }
        ivDrawerList.fastClickListener {
            (requireActivity() as MainActivity).openDrawer()
        }
        mTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                setTabIconVisible(position ?: 0)
            }
        })
    }

    fun setTabIconVisible(position: Int) {
        when (position) {
            0 -> {
                tabOneIcon.visible()
                tabSecondIcon.gone()
                tabThirdIcon.gone()
            }
            1 -> {
                tabOneIcon.gone()
                tabSecondIcon.visible()
                tabThirdIcon.gone()
            }
            2 -> {
                tabOneIcon.gone()
                tabSecondIcon.gone()
                tabThirdIcon.visible()
            }
        }
    }
}