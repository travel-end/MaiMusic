package wind.maimusic.ui.fragment.songlist

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.model.listensong.SongListCover
import wind.maimusic.model.listensong.SongListCovers
import wind.maimusic.ui.adapter.AllSongListAdapter
import wind.maimusic.ui.adapter.OnSongListItemClickListener
import wind.maimusic.utils.LogUtil
import wind.maimusic.utils.getStringRes
import wind.maimusic.utils.isNotNullOrEmpty
import wind.maimusic.vm.AllSongListViewModel
import wind.maimusic.widget.AppBarLayoutStateChangeListener
import wind.widget.effcientrv.addItem
import wind.widget.effcientrv.setText
import wind.widget.effcientrv.setup
import wind.widget.effcientrv.submitList
import wind.widget.utils.loadImg

class AllSongListFragment : BaseLifeCycleFragment<AllSongListViewModel>(),
    OnSongListItemClickListener {
    private lateinit var rvAuthorRecommend: RecyclerView
    private lateinit var rvClassifySongList: RecyclerView
    private lateinit var tabLayoutSongList: TabLayout
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var rootLayout: LinearLayout
    private var rootLayoutHeight: Int = 0
    private var tabLayoutHeight: Int = 0
    private var needExpanded: Boolean = false
    private var appBarExpanded: Boolean = false
    private var allSongListAdapter: AllSongListAdapter? = null
    override fun layoutResId() = R.layout.fragment_all_song_list
    override fun initView() {
        super.initView()
        rvAuthorRecommend = mRootView.findViewById(R.id.all_song_list_rv_author_recommend)
        rvClassifySongList = mRootView.findViewById(R.id.all_song_list_rv_classify)
        tabLayoutSongList = mRootView.findViewById(R.id.all_song_list_tab_classify)
        appBarLayout = mRootView.findViewById(R.id.mAppbarLayout)
        rootLayout = mRootView.findViewById(R.id.root_layout)
        mRootView.findViewById<TextView>(R.id.title_tv).text =
            R.string.song_list_squire.getStringRes()
        initTabLayout()
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        rvAuthorRecommend.setup<SongListCover> {
            withLayoutManager {
                return@withLayoutManager gridLayoutManager
            }
            adapter {
                addItem(R.layout.item_song_list_cover_b) {
                    bindViewHolder { data, position, holder ->
                        data?.let {
                            val item = it as SongListCover
                            setText(R.id.item_song_list_tv_desc, item.listName)
                            itemView?.findViewById<ImageView>(R.id.item_song_list_iv_cover)
                                ?.loadImg(item.cover)
                        }
                    }
                }
            }
        }
    }

    override fun initData() {
        super.initData()
        requireLazyInit()
    }

    override fun lazyInitData() {
        super.lazyInitData()
        mViewModel.initAllSongListData()
    }

    override fun onResume() {
        super.onResume()
        rootLayout.post {
            rootLayoutHeight = rootLayout.height
        }
    }

    private fun refreshAdapter(list: List<SongListCovers>) {
        val bottom = intArrayOf(rootLayoutHeight, tabLayoutHeight)
        if (allSongListAdapter == null) {
            allSongListAdapter = AllSongListAdapter(requireContext(), list, bottom)
            rvClassifySongList.adapter = allSongListAdapter
        } else {
            allSongListAdapter?.refreshData(list)
        }
        allSongListAdapter?.setOnSongListItemClickListener(this)
    }

    override fun initAction() {
        super.initAction()
        tabLayoutSongList.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    val position = tab.position
                    if (!needExpanded) {
                        needExpanded = true
                    } else if (!appBarExpanded) {
                        appBarLayout.setExpanded(false)
                    }
                    if (tab.position != -1) {// 切换recyclerView的位置
                        rvClassifySongList.scrollToPosition(tab.position)
                        val lm = rvClassifySongList.layoutManager as LinearLayoutManager
                        lm.scrollToPositionWithOffset(position, 0)
                    }
                }
            }
        })
        rvClassifySongList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lm = rvClassifySongList.layoutManager as LinearLayoutManager
                val newPosition = lm.findFirstVisibleItemPosition()
                tabLayoutSongList.setScrollPosition(newPosition, 0f, true)
            }
        })
        appBarLayout.addOnOffsetChangedListener(object : AppBarLayoutStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: BarState?) {
                when (state) {
                    BarState.EXPANDED -> {
                        appBarExpanded = false
                    }
                    BarState.COLLAPSED -> {// 折叠状态
                        appBarExpanded = true
                    }
                    BarState.INTERMEDIATE -> {
                        appBarExpanded = false
                    }
                }
            }
        })
    }

    override fun observe() {
        super.observe()
        mViewModel.recommendSongs.observe(this, Observer {
            if (isNotNullOrEmpty(it)) {
                rvAuthorRecommend.submitList(it!!.toMutableList())
            }
        })
        mViewModel.allSongs.observe(this, Observer {
            if (isNotNullOrEmpty(it)) {
                tabLayoutSongList.post {
                    tabLayoutHeight = tabLayoutSongList.height
                    refreshAdapter(it)
                }
            }
        })
    }

    private fun initTabLayout() {
        for (tabName in Constants.tabClassifyName) {
            val tab = tabLayoutSongList.newTab()
            tab.text = tabName
            tabLayoutSongList.addTab(tab)
        }
        setTabWidth()
    }

    /*设置tab宽度和文字一致*/
    private fun setTabWidth() {
        tabLayoutSongList.post {
            try {
                val mTabStrip = tabLayoutSongList.getChildAt(0) as LinearLayout
                for (i in 0 until mTabStrip.childCount) {
                    val tabView = mTabStrip.getChildAt(i)
                    val mTextViewField =
                        tabView.javaClass.getDeclaredField("mTextView")
                    mTextViewField.isAccessible = true
                    val mTextView = mTextViewField[tabView] as TextView
                    tabView.setPadding(0, 0, 0, 0)
                    var width = mTextView.width
                    if (width == 0) {
                        mTextView.measure(0, 0)
                        width = mTextView.measuredWidth
                    }
                    val params = tabView
                        .layoutParams as LinearLayout.LayoutParams
                    params.leftMargin = (tabView.measuredWidth - width) / 2
                    params.rightMargin = (tabView.measuredWidth - width) / 2
                    tabView.layoutParams = params
                    tabView.invalidate()
                }
//                tabLayoutSongList.visibility = View.VISIBLE
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
    }

    override fun onSongListItemClick(item: SongListCover) {
        LogUtil.e("-----AllSongListFragment songListName:${item.listName}")

    }
}