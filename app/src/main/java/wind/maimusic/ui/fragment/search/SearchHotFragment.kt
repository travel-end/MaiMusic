package wind.maimusic.ui.fragment.search

import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.model.searchhot.HistoryTag
import wind.maimusic.model.searchhot.SearchHistory
import wind.maimusic.model.searchhot.SearchHotSong
import wind.maimusic.model.title.HotSearchTitle
import wind.maimusic.vm.SearchHotViewModel
import wind.widget.effcientrv.*

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
        val layoutManager = GridLayoutManager(requireContext(), 2)
        rvSearchHot.setup<Any> {
            withLayoutManager {
                layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (adapter?.getItem(position) is SearchHotSong) {
                            1
                        } else {
                            2
                        }
                    }
                }
                return@withLayoutManager layoutManager
            }
            adapter {
                addItem(R.layout.item_search_hot_history) {
                    isForViewType { data, position -> data is SearchHistory }
                    bindViewHolder { data, position, holder ->
                        val item = data as SearchHistory
                        val rvContent: RecyclerView? = itemView?.findViewById(R.id.item_history_rv)
                        setText(R.id.item_history_title_tv,item.title)
                        rvContent?.let {
                            it.setup<HistoryTag> {
                                dataSource(item.historyList!!)
                                adapter {
                                    addItem(R.layout.item_round_text) {
                                        bindViewHolder { data: Any?, position: Int, holder: ViewHolderCreator<Any> ->
                                            val d = data as HistoryTag
                                            (itemView as TextView).text = d.name
                                        }
                                        itemClicked(View.OnClickListener {

                                        })
                                    }
                                }
                            }
                        }
                    }
                }
                addItem(R.layout.item_common_title) {
                    isForViewType { data, position -> data is HotSearchTitle }
                    bindViewHolder { data, position, holder ->
                        val item = data as HotSearchTitle
                        setText(R.id.item_common_title_tv, item.title)
                    }
                }
                addItem(R.layout.item_search_hot_song) {
                    isForViewType { data, position -> data is SearchHotSong }
                    bindViewHolder { data, position, holder ->
                        val item = data as SearchHotSong
                        setText(R.id.item_search_hot_song_tv_index, "${position - 2}")
                        setText(R.id.item_search_hot_song_tv_song_name, item.hotSongName)
                        if (item.isHot) {
                            setVisible(R.id.item_search_hot_song_tv_status, true)
                            setText(R.id.item_search_hot_song_tv_status, "热")
                        }
                        setText(R.id.item_search_hot_song_tv_description, item.hotSongDescription)
                    }
                }
            }
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.getTempData()
    }

    override fun observe() {
        super.observe()
        mViewModel.hotSearch.observe(this, Observer {
            it?.let {
                rvSearchHot.submitList(it)
            }
        })
    }
}