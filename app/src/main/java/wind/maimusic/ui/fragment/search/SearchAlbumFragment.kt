package wind.maimusic.ui.fragment.search

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.model.core.AlListBean
import wind.maimusic.model.core.ListBean
import wind.maimusic.ui.adapter.AlbumSongsAdapter
import wind.maimusic.ui.adapter.OnAlbumItemClickListener
import wind.maimusic.ui.adapter.SearchSingleSongAdapter
import wind.maimusic.utils.LogUtil
import wind.maimusic.utils.isNotNullOrEmpty
import wind.maimusic.utils.visible
import wind.maimusic.vm.SearchAlbumResultViewModel
import wind.widget.jrecyclerview.adapter.JRefreshAndLoadMoreAdapter

class SearchAlbumFragment:BaseLifeCycleFragment<SearchAlbumResultViewModel>(),OnAlbumItemClickListener {
    private var albumAdapter: AlbumSongsAdapter? = null
    private var jAdapter: JRefreshAndLoadMoreAdapter? = null
    private var pageOffset: Int = 1
    private var mData = mutableListOf<AlListBean>()
    private var searchText: String = ""
    private var hasLoadData:Boolean = false
    private lateinit var rvAlbum:RecyclerView
    companion object {
        fun newInstance(searchText:String):SearchAlbumFragment {
            val fragment = SearchAlbumFragment()
            val bundle = Bundle().apply {
                putString(Constants.KEY_SEARCH_CONTENT,searchText)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun layoutResId()=R.layout.fragment_search_album
    override fun initView() {
        super.initView()
        rvAlbum = mRootView.findViewById(R.id.rv_search_album)
        rvAlbum.setHasFixedSize(true)
    }

    override fun onResume() {
        super.onResume()
        if (!hasLoadData) {// 这里需要做一个懒加载 只有在完全滑动至可见的时候才加载数据
            LogUtil.e("--------onResume----")
            hasLoadData = true
            searchText = arguments?.getString(Constants.KEY_SEARCH_CONTENT) ?: ""
            if (searchText.isNotEmpty()) {
                mViewModel.searchAlbum(searchText, pageOffset)
            }
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.searchAlbumResult.observe(this,Observer{
            it?.let {
                val song = it.data?.album
                if (song != null) {
                    val songList = song.list
                    if (isNotNullOrEmpty(songList)) {
                        if (mData.isEmpty()) {// 初始加载
                            hideLoadingResultView()
                            mData.addAll(songList!!)
                            albumAdapter = AlbumSongsAdapter(requireContext(), searchText,mData)
                            albumAdapter?.setOnAlbumItemClickListener(this@SearchAlbumFragment)
                            jAdapter = JRefreshAndLoadMoreAdapter(requireContext(), albumAdapter)
                            jAdapter?.setOnLoadMoreListener {
                                pageOffset += 1
                                mViewModel.searchAlbum(searchText, pageOffset)
                            }
                            jAdapter?.setIsOpenRefresh(false)
                            rvAlbum.adapter = jAdapter
                            rvAlbum.visible()
                        } else {
                            val size = mData.size
                            val songListSize = songList!!.size
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
    }

    override fun onAlbumItemClick(album: AlListBean, position: Int) {

    }
}