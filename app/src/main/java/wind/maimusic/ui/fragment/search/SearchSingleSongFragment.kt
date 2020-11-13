package wind.maimusic.ui.fragment.search

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseSongListFragment
import wind.maimusic.model.core.ListBean
import wind.maimusic.ui.activities.MainActivity
import wind.maimusic.ui.adapter.OnSingleSongItemClickListener
import wind.maimusic.ui.adapter.SearchSingleSongAdapter
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
class SearchSingleSongFragment : BaseSongListFragment<SearchResultViewModel>(),
    OnSingleSongItemClickListener {
    private var searchText: String = ""
    private var rawAdapter: SearchSingleSongAdapter? = null
    private var jAdapter: JRefreshAndLoadMoreAdapter? = null
    private var pageOffset: Int = 1
    private var mData = mutableListOf<ListBean>()

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
        rvSongList.setHasFixedSize(true)
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
                        if (mData.isEmpty()) {// 初始加载
                            hideLoadingResultView()
                            flPlayAll.visible()
                            mData.addAll(songList!!)
                            rawAdapter = SearchSingleSongAdapter(requireContext(), searchText,mData)
                            rawAdapter?.setOnSingleSongItemClickListener(this@SearchSingleSongFragment)
                            jAdapter = JRefreshAndLoadMoreAdapter(requireContext(), rawAdapter)
                            jAdapter?.setOnLoadMoreListener {
                                pageOffset += 1
                                mViewModel.searchSong(searchText, pageOffset)
                            }
                            jAdapter?.setIsOpenRefresh(false)
                            rvSongList.adapter = jAdapter
                            rvSongList.visible()
                            mViewModel.addOneSearchTag(searchText)
                        } else {
                            val size = mData.size
                            val songListSize = songList!!.size
//                            LogUtil.e("size=$size")
//                            LogUtil.e("songList size=$songListSize")
                            mData.addAll(songList)
                            jAdapter?.setLoadComplete()
                            if (songListSize < Constants.SEARCH_SONG_PAGE_SIZE) {
                                if (songListSize == 0) {
                                    jAdapter?.setNoMore()
                                } else {
                                    jAdapter?.notifyItemRangeInserted(
                                        jAdapter?.getRealPosition(size)!!,
                                        songListSize
                                    )
                                }
                            } else {
                                jAdapter?.notifyItemRangeInserted(
                                    jAdapter?.getRealPosition(size)!!,
                                    Constants.SEARCH_SONG_PAGE_SIZE
                                )
                            }
                        }
                    } else {
                        if (mData.isNotEmpty()) {
                            jAdapter?.setNoMore()
                        }
                    }
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

    override fun onSingleSongItemClick(song: ListBean, position: Int) {
        (requireActivity() as MainActivity).showLoadingNormal("")
        val currentSearchSong = SongUtil.assemblySong(song, Consts.ONLINE_SEARCH)
        mViewModel.getSongPlayUrl(currentSearchSong)
    }

    override fun onRightFunctionClick(view: View) {
        TODO("Not yet implemented")
    }
}