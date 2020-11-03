package wind.maimusic.ui.fragment

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import com.zinc.jrecycleview.JRecycleView
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.model.listensong.*
import wind.maimusic.model.title.ListenSongListTitle
import wind.maimusic.model.title.PoetrySongTitle
import wind.maimusic.model.title.SingleSongTitle
import wind.maimusic.utils.inflate
import wind.maimusic.vm.ListenSongViewModel
import wind.widget.effcientrv.*
import wind.widget.utils.loadImg

/**
 * 听歌tab
 * 页面数据类型分析
 * 一楼banner、 图片+文字（可变项）
 * 二楼5个tab（固定项）
 * 三楼标题：精选歌单
 * 四楼歌单推荐（可变向）
 * 五楼标题：单曲推荐
 * 六楼推荐单曲
 * 七楼标题：诗＆歌
 * 八楼诗＆歌推荐
 */
class ListenSongFragment:BaseLifeCycleFragment<ListenSongViewModel> (){
    private lateinit var rvListenSong:JRecycleView
    private var listenSongAdapter:EfficientAdapter<Any>?=null
    override fun layoutResId()=R.layout.fragment_listen_song
    companion object {
        fun newInstance() : Fragment {
            return ListenSongFragment()
        }
    }

    override fun initView() {
        super.initView()
        rvListenSong = mRootView.findViewById(R.id.rv_listen)
        val lm = GridLayoutManager(requireContext(),5)
        lm.spanSizeLookup = object :GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (listenSongAdapter?.getItem(position) is TabMenu) 1 else 5
            }
        }
        rvListenSong.layoutManager = lm
        listenSongAdapter = efficientAdapter<Any> {
            addItem(R.layout.item_horiz_rv) {
                isForViewType { data, position -> data is Banner }
                bindViewHolder { data, position, holder ->
                    data?.let {
                        val bannerList = (it as Banner).bannerList
                        val rvBanner = (itemView as RecyclerView)
                        rvBanner.onFlingListener = null
                        val pagerSnap = PagerSnapHelper()
                        pagerSnap.attachToRecyclerView(rvBanner)
                        rvBanner.adapter = object :RecyclerView.Adapter<BannerViewHolder>() {
                            override fun onCreateViewHolder(
                                parent: ViewGroup,
                                viewType: Int
                            ): BannerViewHolder {
                                return BannerViewHolder(R.layout.item_banner.inflate(parent))
                            }
                            override fun getItemCount()= Int.MAX_VALUE
                            override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
                                val banner =bannerList[position % bannerList.size]
                                holder.bannerIv.loadImg(banner.imgUrl, 8f)
                                holder.bannerTvName.text = banner.title
                            }

                        }

                    }

                }
            }
            addItem(R.layout.item_tab_menu) {
                isForViewType { data, position -> data is TabMenu }
                bindViewHolder { data, position, holder ->
                    val menu = data as TabMenu
                    setText(R.id.item_tab_menu_name,menu.menuName)
                }
            }
            addItem(R.layout.item_common_title) {
                isForViewType { data, position -> data is ListenSongListTitle }
                bindViewHolder { data, position, holder ->
                    val songListTitle = data as ListenSongListTitle
                    setText(R.id.item_common_title_tv,songListTitle.title)
                    setText(R.id.item_common_end_tv,songListTitle.text)
                }
            }
            addItem(R.layout.item_horiz_rv) {
                isForViewType { data, _ -> data is SongListCovers }
                bindViewHolder { data, _, _ ->
                    val listCovers = data as SongListCovers
                    val rvCover = itemView as RecyclerView
                    rvCover.onFlingListener=null
                    val snapHelper = PagerSnapHelper()
                    snapHelper.attachToRecyclerView(rvCover)
                    val adapter = efficientAdapter<SongListCover> {
                        addItem(R.layout.item_song_list_cover) {
                            bindViewHolder { data: SongListCover?, position: Int, holder: ViewHolderCreator<SongListCover> ->
                            data?.let {
                                holder.itemView?.findViewById<ImageView>(R.id.item_song_list_iv_cover)?.loadImg(
                                    it.cover
                                )
                                setText(R.id.item_song_list_tv_desc,it.listName)
                                itemClicked(View.OnClickListener {
                                    Log.e("JG","${data.listName}")
                                })
                            }
                            }
                        }
                    }.attach(rvCover)
                    adapter.submitList(listCovers.listCovers.toMutableList())
                }
            }
            addItem(R.layout.item_change_title) {
                isForViewType { data, position -> data is SingleSongTitle }
                bindViewHolder { data, position, holder ->
                    val title  = data as SingleSongTitle
                    setText(R.id.find_tv_title_three,title.title)
                    setText(R.id.find_change_second_three,title.text)
                }
            }
            addItem(R.layout.item_single_song) {
                isForViewType { data, position -> data is SingleSong }
                bindViewHolder { data, position, holder ->
                    data?.let {
                        val singleSong = it as SingleSong
                        holder.itemView?.findViewById<ImageView>(R.id.single_song_iv_cover)?.loadImg(singleSong.cover,6f)
                        setText(R.id.single_song_tv_name,singleSong.songName)
                        setText(R.id.single_song_tv_singer,singleSong.singer)
                        setText(R.id.single_song_tv_desc,singleSong.desc)
                    }
                }
            }
            addItem(R.layout.item_poetry_song_title) {
                isForViewType { data, position -> data is PoetrySongTitle }
                bindViewHolder { data, position, holder ->
                    data?.let {
                        val title = it as PoetrySongTitle
                        setText(R.id.find_tv_poetry,title.title)
                    }
                }
            }
            addItem(R.layout.item_poetry_song) {
                isForViewType { data, position -> data is PoetrySong }
                bindViewHolder { data, position, holder ->
                    data?.let {
                        val poetrySong = it as PoetrySong
                        val leftIv = itemView?.findViewById<ShapeableImageView>(R.id.find_sp_iv_icon)
                        leftIv?.shapeAppearanceModel = ShapeAppearanceModel.Builder()
                            .setAllCornerSizes(ShapeAppearanceModel.PILL).build()
                        leftIv?.loadImg(poetrySong.cover)
                        setText(R.id.find_sp_tv_title,poetrySong.name)
                        setText(R.id.find_tv_sp_author,poetrySong.singer)
                        setText(R.id.poetry_song_tv_desc,poetrySong.desc)
                    }
                }
            }

        }.attach(rvListenSong)
    }

    override fun initData() {
        super.initData()
        mViewModel.getListenData()
    }

    override fun observe() {
        super.observe()
        mViewModel.listData.observe(this, Observer {
            it?.let {
                rvListenSong.submitList(it)
            }
        })
    }

    inner class BannerViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val bannerIv :ImageView = itemView.findViewById(R.id.item_banner_iv)
        val bannerTvName:TextView = itemView.findViewById(R.id.item_banner_tv_name)
    }
}