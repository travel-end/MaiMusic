package wind.maimusic.ui.fragment.search

import android.os.Bundle
import androidx.lifecycle.Observer
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseSongListFragment
import wind.maimusic.model.core.ListBean
import wind.maimusic.utils.*
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

    override fun initView() {
        super.initView()
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
//                            if (position == lastPosition) {
//                                setTextColor(R.id.item_search_song_list_tv_song_name,R.color.colorPrimary.getColorRes())
//                                setTextColor(R.id.item_search_song_list_tv_song_singer,R.color.colorPrimary.getColorRes())
//                                setTextColor(R.id.item_search_song_list_tv_song_album,R.color.colorPrimary.getColorRes())
//                                setTextColor(R.id.item_search_song_list_tv_song_lyric,R.color.colorPrimary.getColorRes())
//                            } else {
//                                setTextColor(R.id.item_search_song_list_tv_song_name,R.color.black2.getColorRes())
//                                setTextColor(R.id.item_search_song_list_tv_song_singer,R.color.colorPrimaryLight2.getColorRes())
//                                setTextColor(R.id.item_search_song_list_tv_song_album,R.color.text_color.getColorRes())
//                                setTextColor(R.id.item_search_song_list_tv_song_lyric,R.color.text_color.getColorRes())
//                            }
//

                            (itemView as RippleView).setOnRippleCompleteListener {
//                                checkPosition(position,adapter)
//                                if (position != lastPosition) {
//                                    notifyItemChanged(position)
//                                    lastPosition = position
//                                    
//                                }
                                // TODO: 2020/10/29 to playactivity 
                                
                                val song= Song().apply {
                                    songId = data.songmid //004DrG5A2nm7q2
                                    singer = StringUtil.getSinger(s)// 鸾音社
                                    songName = data.songname// 夜来寒雨晓来风
                                    imgUrl = "${Consts.ALBUM_PIC}${data.albummid}${Consts.JPG}"////http://y.gtimg.cn/music/photo_new/T002R180x180M000004UvnL62KXhCQ.jpg
                                    duration = data.interval//187  (秒)
                                    isOnline = true
                                    mediaId = data.strMediaMid//004DrG5A2nm7q2
                                    albumName = data.albumname//夜来寒雨晓来风
                                    isDownload = DownloadedUtil.isExistOfDownloadSong(data.songmid?:"")//003IHI2x3RbXLS  // 是否已经下载过了（初次搜索为false）
                                }
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
        LogUtil.e("searchText:$searchText")
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
    }

}