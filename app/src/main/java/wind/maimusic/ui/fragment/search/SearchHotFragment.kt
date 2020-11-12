package wind.maimusic.ui.fragment.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.model.searchhot.*
import wind.maimusic.model.title.PoetrySongTitle
import wind.maimusic.model.title.SingleSongTitle
import wind.maimusic.model.title.Title
import wind.maimusic.utils.LogUtil
import wind.maimusic.utils.getStringRes
import wind.maimusic.utils.gone
import wind.maimusic.utils.isNotNullOrEmpty
import wind.maimusic.vm.SearchHotViewModel
import wind.widget.effcientrv.*
import wind.widget.flowlayout.FlowLayout
import wind.widget.flowlayout.TagAdapter
import wind.widget.flowlayout.TagFlowLayout
import wind.widget.utils.fastClickListener

/**
 * @By Journey 2020/10/27
 * @Description
 */
class SearchHotFragment : BaseLifeCycleFragment<SearchHotViewModel>() {
    private lateinit var rvSearchHot: RecyclerView
    companion object {
        fun newInstance(): SearchHotFragment {
            return SearchHotFragment()
        }
    }

    override fun layoutResId() = R.layout.fragment_search_hot

    override fun initView() {
        super.initView()
        rvSearchHot = mRootView.findViewById(R.id.search_hot_rv)
        rvSearchHot.setup<Any> {
            adapter {
                //2
                addItem(R.layout.item_flow_layout) {
                    isForViewType { data, position -> data is SearchHistory }
                    bindViewHolder { data, position, holder ->
                        val searchHistory = (data as SearchHistory).historyList
                        setText(R.id.item_history_title_tv,R.string.search_history.getStringRes())
                        if (isNotNullOrEmpty(searchHistory)) {
                            itemView?.findViewById<TagFlowLayout>(R.id.item_sh_flow_layout)
                                ?.let { flow ->
                                    flow.adapter =
                                        object : TagAdapter<HistoryTag>(searchHistory) {
                                            override fun getView(
                                                parent: FlowLayout?,
                                                position: Int,
                                                t: HistoryTag?
                                            ): View {
                                                return LayoutInflater.from(parent?.context)
                                                    .inflate(
                                                        R.layout.item_round_text,
                                                        parent,
                                                        false
                                                    )
                                                    .apply { (this as TextView).text = t?.name }
                                            }
                                        }
                                    flow.setOnTagClickListener { _, position, _ ->
                                        LogUtil.e("searchHistory name:${searchHistory!![position].name}")
                                        false
                                    }
                                    setVisible(R.id.item_sh_flow_layout, true)
                                }
                        } else {
                            visible(R.id.item_sh_tv_no)
                            invisible(R.id.item_history_end_iv)
                            itemView?.findViewById<TagFlowLayout>(R.id.item_sh_flow_layout)?.removeAllViews()
                            itemView?.findViewById<TagFlowLayout>(R.id.item_sh_flow_layout)?.gone()
                        }
                        clicked(R.id.item_history_end_iv,View.OnClickListener {
                            mViewModel.deleteSearchHistoryTag()
                        })
                    }
                }

                //4
                addItem(R.layout.item_flow_layout) {
                    isForViewType { data, position -> data is RecommendSearchList }
                    bindViewHolder { data, position, holder ->
                        val recommendList = (data as RecommendSearchList).recommendSearch
                        LogUtil.e("recommendList----:${recommendList.size}")
                        setText(R.id.item_history_title_tv,R.string.recommend_search.getStringRes())
                        invisible(R.id.item_history_end_iv)
                        if (isNotNullOrEmpty(recommendList)) {
                            itemView?.findViewById<TagFlowLayout>(R.id.item_sh_flow_layout)
                                ?.let { flow ->
                                    flow.adapter =
                                        object : TagAdapter<RecommendSearch>(recommendList) {
                                            override fun getView(
                                                parent: FlowLayout?,
                                                position: Int,
                                                t: RecommendSearch?
                                            ): View {
                                                return LayoutInflater.from(parent?.context)
                                                    .inflate(
                                                        R.layout.item_round_text,
                                                        parent,
                                                        false
                                                    )
                                                    .apply { (this as TextView).text = t?.tagName }
                                            }
                                        }
                                    flow.setOnTagClickListener { _, position, _ ->
                                        LogUtil.e("recommendList name:${recommendList[position].tagName}")
                                        false
                                    }
                                    setVisible(R.id.item_sh_flow_layout, true)
                                }
                        }
                    }
                }
                // 5
                addItem(R.layout.item_search_history_title) {
                    isForViewType { data, _ -> data is SingleSongTitle }
                    bindViewHolder { data, position, holder ->
                        invisible(R.id.item_history_end_iv)
                        setText(R.id.item_history_title_tv, (data as SingleSongTitle).title)
                    }
                }
                // 6
                addItem(R.layout.item_search_hot_song_bg) {
                    isForViewType { data, position -> data is HotSearchSongList }
                    bindViewHolder { data, position, holder ->
                        val songList = (data as HotSearchSongList).hotSearchSongs
                        val rootView = itemView as LinearLayout
                        if (isNotNullOrEmpty(songList)) {
                            rootView.removeAllViews()
                            val lps = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            lps.setMargins(0, 10, 0, 10)
                            for (song in songList) {
                                val itemSongView = View.inflate(
                                    requireContext(),
                                    R.layout.item_search_hot_song,
                                    null
                                )
                                val tvSongName: TextView =
                                    itemSongView.findViewById(R.id.item_search_hot_song_tv_song_name)
                                val tvDesc: TextView =
                                    itemSongView.findViewById(R.id.item_search_hot_song_tv_description)
                                val tvIndex: TextView =
                                    itemSongView.findViewById(R.id.item_search_hot_song_tv_index)
                                tvSongName.text = song.songName
                                tvDesc.text = song.desc
                                tvIndex.text = "${song.id ?: 0}"
                                rootView.addView(itemSongView, lps)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.initHotSearchData()
    }

    override fun observe() {
        super.observe()
        mViewModel.mData.observe(this, Observer {
            it?.let {
                rvSearchHot.submitList(it)
            }
        })
        mViewModel.deleteHistory.observe(this,Observer{
            if (it == true) {
                rvSearchHot.updateData(0,SearchHistory())
            }
        })
    }
}