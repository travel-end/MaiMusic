package wind.maimusic.ui.fragment.search

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseSongListFragment
import wind.maimusic.model.OnlineSong
import wind.maimusic.model.core.AlbumListBean
import wind.maimusic.ui.fragment.songlist.SongListTopDetailFragment
import wind.maimusic.utils.*
import wind.maimusic.vm.AlbumSongViewModel
import wind.widget.cost.Consts
import wind.widget.effcientrv.*
import wind.widget.model.Song
import wind.widget.utils.fastClickListener

/**
 * @By Journey 2020/11/13
 * @Description
 */
class SearchAlbumSongFragment:BaseSongListFragment<AlbumSongViewModel>() {
    private var albumName:String?=null
    private var singerName:String?=null
    private var albumCover:String?=null
    private var publicTime:String?=null
    private var albumId:String?=null


    private var detailName:String=""
    private var detailLanguage:String=""
    private var detailCompany:String=""
    private var detailDesc:String=""
    private var detailPublicTime:String=""
    private var detailType:String=""
    override fun songListType()= -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundle()
    }

    override fun layoutResId()=R.layout.fragment_md_style_song_list

    override fun initView() {
        super.initView()
        val target = object :SimpleTarget<Drawable>(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL) {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                val bitmap = (resource as BitmapDrawable).bitmap
                if (bitmap != null) {
                    ivSongListLargeCover?.setImageDrawable(BitmapUtil.getSongListBackground(requireContext(),bitmap))
                    ivSongListSmallCover?.setImageBitmap(bitmap)
                }
            }
        }
        // TODO: 2020/11/13 默认背景重新设置
        if (albumCover.isNotNullOrEmpty()) {
            Glide.with(requireActivity())
                .load(albumCover)
                .apply(RequestOptions.placeholderOf(R.drawable.shape_translate_rect_bg))
                .apply(RequestOptions.errorOf(R.drawable.shape_translate_rect_bg))
                .into(target)
        }
        tvSongListAuthor?.text = singerName
        tvSongListName?.text = albumName
        tvSongListTitleName?.text = albumName
        tvSongListDescription?.text = "${R.string.public_time.getStringRes()} $publicTime"
    }

    override fun initData() {
        super.initData()
        requireLazyInit()
        tvSongListName?.fastClickListener {
            it.nav(R.id.album_to_album_top_detail_fragment,setBundle())
        }
    }

    override fun lazyInitData() {
        super.lazyInitData()
        if (albumId.isNotNullOrEmpty()) {
            mViewModel.getAlbumSongs(albumId!!)
        }
    }
    override fun setRvContent() {
        rvSongList.setHasFixedSize(true)
        rvSongList.setup<AlbumListBean> {
            adapter {
                addItem(R.layout.item_song_list) {
                    bindViewHolder { data, pos, _ ->
                        data?.let {albumSong->
                            setText(R.id.item_song_list_tv_song_name, albumSong.songname)
                            setText(R.id.item_song_list_tv_song_singer, StringUtil.getSinger(albumSong))
                            val hadDownloaded = if (albumSong.songmid == null) false else mViewModel.findIsDownloaded(albumSong.songmid!!)
                            setVisible(R.id.item_song_list_iv_downloaded, hadDownloaded)
                            val song = SongUtil.getSong()
                            val currentSongId = song?.songId
                            if (currentSongId != null) {
                                if (currentSongId == albumSong.songmid) {
                                    setVisible(R.id.item_song_list_iv_playing, true)
                                    lastPosition = pos
                                } else {
                                    setVisible(R.id.item_song_list_iv_playing, false)
                                }
                            }
                            itemClicked(View.OnClickListener {
                                if (pos != lastPosition) {
                                    notifyItemChanged(lastPosition)
                                    lastPosition = pos
                                }
                                notifyItemChanged(pos)
                                val s = Song().apply {
                                    songId = albumSong.songmid
                                    singer = StringUtil.getSinger(albumSong)
                                    songName = albumSong.songname
                                    position = pos
                                    duration = albumSong.interval?:0
                                    isOnline = true
                                    listType = Consts.LIST_TYPE_ONLINE
                                    onlineSubjectType = Consts.JUST_ONLINE_SONG
                                    imgUrl = "${Consts.ALBUM_PIC}${albumSong.albummid}${Consts.JPG}"
                                    mediaId = albumSong.strMediaMid
                                    isDownload = hadDownloaded
                                }
                                SongUtil.saveSong(s)
                                playerBinder?.play(s.listType,Consts.JUST_ONLINE_SONG)
                            })
                            clicked(R.id.item_song_list_iv_more,View.OnClickListener {
                                bottomFunctionDialog?.show()
                            })
                        }
                    }
                }
            }
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.albumSongs.observe(this,Observer{
            if (isNotNullOrEmpty(it)) {
                rvSongList.submitList(it!!.toMutableList())
                rvSongList.visible()
                flPlayAll.visible()
            }
        })
        mViewModel.albumDetail.observe(this,Observer{
            detailName = it.songListName?:""
            detailLanguage = it.language?:""
            detailCompany = it.company?:""
            detailDesc = it.songListDescription?:""
            detailType = it.type?:""
        })
    }

    private fun getBundle() {
        val b = arguments
        b?.let {
            albumId = it.getString(Constants.ALBUM_ID)
            albumName = it.getString(Constants.ALBUM_NAME)
            singerName = it.getString(Constants.ALBUM_SINGER)
            albumCover = it.getString(Constants.ALBUM_COVER)
            publicTime = it.getString(Constants.ALBUM_PUBLIC_TIME)
        }
    }
    private fun setBundle():Bundle {
        return bundleOf(
            SongListTopDetailFragment.DETAIL_COVER to albumCover,
            SongListTopDetailFragment.DETAIL_NAME to detailName,
            SongListTopDetailFragment.DETAIL_LANGUAGE to detailLanguage,
            SongListTopDetailFragment.DETAIL_COMPANY to detailCompany,
            SongListTopDetailFragment.DETAIL_DESC to detailDesc,
            SongListTopDetailFragment.DETAIL_PUBLIC_TIME to detailPublicTime,
            SongListTopDetailFragment.DETAIL_TYPE to detailType
        )
    }
}