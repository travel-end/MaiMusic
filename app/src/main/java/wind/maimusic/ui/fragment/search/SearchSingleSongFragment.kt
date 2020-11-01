package wind.maimusic.ui.fragment.search

import android.os.Bundle
import androidx.lifecycle.Observer
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseSongListFragment
import wind.maimusic.model.core.ListBean
import wind.maimusic.utils.SongUtil
import wind.maimusic.utils.StringUtil
import wind.maimusic.utils.isNotNullOrEmpty
import wind.maimusic.utils.visible
import wind.maimusic.vm.SearchResultViewModel
import wind.widget.cost.Consts
import wind.widget.effcientrv.*
import wind.widget.model.Song
import wind.widget.rippleview.RippleView

class SearchSingleSongFragment:BaseSongListFragment<SearchResultViewModel>() {
    private var searchText:String = ""
    companion object {
        fun newInstance(searchText:String):SearchSingleSongFragment {
            val fragment = SearchSingleSongFragment()
            val bundle = Bundle().apply {
                putString(Constants.KEY_SEARCH_CONTENT,searchText)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun layoutResId()=R.layout.fragment_search_single_song

    override fun setRvContent() {
        rvSongList.setup<ListBean> {
            adapter {
                addItem(R.layout.item_search_song) {
                    bindViewHolder { data, position, holder ->
                        data?.let {s->
                            setText(R.id.item_search_song_list_tv_song_name,s.songname)
                            setText(R.id.item_search_song_list_tv_song_singer,StringUtil.getSinger(s))
                            setText(R.id.item_search_song_list_tv_song_album,s.albumname)
                            setVisible(R.id.item_search_song_list_iv_downloaded,false) // TODO: 2020/10/28
                            if (s.lyric.isNotNullOrEmpty()) {
                                setVisible(R.id.item_search_song_list_tv_song_lyric,true)
                                setText(R.id.item_search_song_list_tv_song_lyric,s.lyric)
                            }
                            (itemView as RippleView).setOnRippleCompleteListener {
                                // TODO: 2020/10/29 to playactivity
                                val song = SongUtil.assemblySong(s,Consts.LIST_TYPE_ONLINE)
                                mViewModel.getSongPlayUrl(song)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun initData() {
        super.initData()
        searchText = arguments?.getString(Constants.KEY_SEARCH_CONTENT)?:""
        if (searchText.isNotEmpty()) {
            mViewModel.searchSong(searchText,1)
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.searchResult.observe(this,Observer{
            it?.let {
                val song = it.data?.song
                if (song != null) {
                    val songList = song.list
                    if (isNotNullOrEmpty(songList)) {
                        hideLoadingResultView()
                        flPlayAll.visible()
                        rvSongList.submitList(songList!!.toMutableList())
                        rvSongList.visible()
                    }
                }
            }
        })
        mViewModel.songPlayUrlResult.observe(this,Observer{
            it?.let {
                val song = it.entries.find { entry ->
                    entry.key == "song"
                }?.value as Song
                val url = it.entries.find { entry ->
                    entry.key=="url"
                }?.value as String
                song.url = url
                SongUtil.saveSong(song)
                playerBinder?.playOnline()
            }
        })
    }
    override fun songListType()=0
}