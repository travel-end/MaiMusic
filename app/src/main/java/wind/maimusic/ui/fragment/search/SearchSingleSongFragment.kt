package wind.maimusic.ui.fragment.search

import android.os.Bundle
import androidx.lifecycle.Observer
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseSongListFragment
import wind.maimusic.model.core.ListBean
import wind.maimusic.ui.activities.MainActivity
import wind.maimusic.utils.*
import wind.maimusic.vm.SearchResultViewModel
import wind.widget.cost.Consts
import wind.widget.effcientrv.*
import wind.widget.jrecyclerview.adapter.JRefreshAndLoadMoreAdapter
import wind.widget.model.Song
import wind.widget.rippleview.RippleView

/**
 * 歌曲搜索结果（单曲）
 */
class SearchSingleSongFragment : BaseSongListFragment<SearchResultViewModel>() {
    private var searchText: String = ""
    private var rawAdapter: EfficientAdapter<ListBean>? = null
    private var jAdapter: JRefreshAndLoadMoreAdapter? = null
    private var pageOffset: Int = 1
    private var dataList = mutableListOf<ListBean>()

    companion object {
        fun newInstance(searchText: String): SearchSingleSongFragment {
            val fragment = SearchSingleSongFragment()
            val bundle = Bundle().apply {
                putString(Constants.KEY_SEARCH_CONTENT, searchText)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun layoutResId() = R.layout.fragment_search_single_song
    override fun setRvContent() {
        rawAdapter = efficientAdapter {
            addItem(R.layout.item_search_song) {
                bindViewHolder { data, _, _ ->
                    data?.let { s ->
                        setText(R.id.item_search_song_list_tv_song_name, s.songname)
                        setText(
                            R.id.item_search_song_list_tv_song_singer,
                            StringUtil.getSinger(s)
                        )
                        setText(R.id.item_search_song_list_tv_song_album, s.albumname)
                        setVisible(
                            R.id.item_search_song_list_iv_downloaded,
                            false
                        ) // TODO: 2020/10/28
                        if (s.lyric.isNotNullOrEmpty()) {
                            setVisible(R.id.item_search_song_list_tv_song_lyric, true)
                            setText(R.id.item_search_song_list_tv_song_lyric, s.lyric)
                        }
                        (itemView as RippleView).setOnRippleCompleteListener {
                            (requireActivity() as MainActivity).showLoadingNormal("")
                            val song = SongUtil.assemblySong(s, Consts.ONLINE_SEARCH)
                            mViewModel.getSongPlayUrl(song)
                        }
                    }
                }
            }
        }
        jAdapter = JRefreshAndLoadMoreAdapter(requireContext(), rawAdapter)
        jAdapter?.setIsOpenRefresh(false)
        jAdapter?.setOnLoadMoreListener {
            LogUtil.e("--setOnLoadMoreListener--")
            pageOffset += 1
            mViewModel.searchSong(searchText, pageOffset)
        }
        rvSongList.adapter = jAdapter
    }

    override fun initData() {
        super.initData()
        searchText = arguments?.getString(Constants.KEY_SEARCH_CONTENT) ?: ""
        if (searchText.isNotEmpty()) {
            mViewModel.searchSong(searchText, pageOffset)
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.searchResult.observe(this, Observer {
            it?.let {
                val song = it.data?.song
                if (song != null) {
                    val songList = song.list
                    if (isNotNullOrEmpty(songList)) {
                        hideLoadingResultView()
                        val size = songList!!.size
                        flPlayAll.visible()
                        dataList.addAll(songList)
                        LogUtil.e("----SearchSingleSongFragment observe dataList size--:${dataList.size}")
                        if (pageOffset == 1) {
                            rawAdapter?.items = dataList
                            jAdapter?.notifyDataSetChanged()
                        } else {
                            jAdapter?.setLoadComplete()
                            if (size == 0) {
                                jAdapter?.setNoMore()
                            } else {
                                jAdapter?.notifyItemRangeInserted(jAdapter?.getRealPosition(size)!!, size)
                            }
                        }
                        rvSongList.visible()
                    }
                    mViewModel.addOneSearchTag(searchText)
                }
            }
        })
        mViewModel.songPlayUrlResult.observe(this, Observer {
            (requireActivity() as MainActivity).dismissLoadingNormal()
            it?.let {
                val song = it.entries.find { entry ->
                    entry.key == "song"
                }?.value as Song
                val url = it.entries.find { entry ->
                    entry.key == "url"
                }?.value as String
                song.url = url
                SongUtil.saveSong(song)
                playerBinder?.playOnline()
                requireActivity().toPlayAct(Consts.SONG_PLAY)
            }
        })
    }

    override fun songListType() = 0
    override fun initStatusBar() {
    }
}