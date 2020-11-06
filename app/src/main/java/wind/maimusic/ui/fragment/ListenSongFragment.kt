package wind.maimusic.ui.fragment

import android.content.Intent
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
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.model.listensong.*
import wind.maimusic.model.title.ListenSongListTitle
import wind.maimusic.model.title.PoetrySongTitle
import wind.maimusic.model.title.SingleSongTitle
import wind.maimusic.ui.activities.TestActivity
import wind.maimusic.utils.LogUtil
import wind.maimusic.utils.inflate
import wind.maimusic.utils.isNotNullOrEmpty
import wind.maimusic.utils.toSongList
import wind.maimusic.vm.ListenSongViewModel
import wind.widget.effcientrv.*
import wind.widget.jrecyclerview.JRecycleView
import wind.widget.jrecyclerview.adapter.JRefreshAndLoadMoreAdapter
import wind.widget.utils.fastClickListener
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
class ListenSongFragment : BaseLifeCycleFragment<ListenSongViewModel>() {
    private lateinit var rvListenSong: JRecycleView
    private var rawAdapter: EfficientAdapter<Any>? = null
    private var jAdapter: JRefreshAndLoadMoreAdapter? = null
    override fun layoutResId() = R.layout.fragment_listen_song

    companion object {
        fun newInstance(): Fragment {
            return ListenSongFragment()
        }
    }

    override fun initView() {
        super.initView()
        rvListenSong = mRootView.findViewById(R.id.rv_listen)
        val lm = GridLayoutManager(requireContext(), 5)
        lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                /*position-1:去除刷新头*/
                return if (rawAdapter?.getItem(position - 1) is TabMenu) 1 else 5
            }
        }
        rvListenSong.layoutManager = lm
        rawAdapter = efficientAdapter<Any> {
            addItem(R.layout.item_horiz_rv) {
                isForViewType { data, position -> data is Banner }
                bindViewHolder { data, position, holder ->
                    data?.let {
                        val bannerList = (it as Banner).bannerList
                        val rvBanner = (itemView as RecyclerView)
                        rvBanner.onFlingListener = null
                        val pagerSnap = PagerSnapHelper()
                        pagerSnap.attachToRecyclerView(rvBanner)
                        rvBanner.adapter = object : RecyclerView.Adapter<BannerViewHolder>() {
                            override fun onCreateViewHolder(
                                parent: ViewGroup,
                                viewType: Int
                            ): BannerViewHolder {
                                return BannerViewHolder(R.layout.item_banner.inflate(parent))
                            }

                            override fun getItemCount() = Int.MAX_VALUE
                            override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
                                val banner = bannerList[position % bannerList.size]
                                holder.bannerIv.loadImg(banner.imgUrl, 8f)
                                holder.bannerTvName.text = banner.title
                                holder.itemView.fastClickListener {
                                    requireActivity().startActivity(
                                        Intent(
                                            requireContext(),
                                            TestActivity::class.java
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
            addItem(R.layout.item_tab_menu) {
                isForViewType { data, _ -> data is TabMenu }
                bindViewHolder { data, position, holder ->
                    itemView?.findViewById<ShapeableImageView>(R.id.item_tab_menu_icon)?.shapeAppearanceModel =
                        ShapeAppearanceModel.Builder().setAllCornerSizes(ShapeAppearanceModel.PILL)
                            .build()
                    val menu = data as TabMenu
                    setText(R.id.item_tab_menu_name, menu.menuName)
                    itemClicked(View.OnClickListener {
                        when(position) {
                            1->{//每日推荐
                                it.toSongList(Constants.ST_DAILY_RECOMMEND)
                            }
                        }
                    })
                }
            }
            addItem(R.layout.item_common_title) {
                isForViewType { data, _ -> data is ListenSongListTitle }
                bindViewHolder { data, position, holder ->
                    val songListTitle = data as ListenSongListTitle
                    setText(R.id.item_common_title_tv, songListTitle.title)
                    setText(R.id.item_common_end_tv, songListTitle.text)
                }
            }
            addItem(R.layout.item_horiz_rv) {
                isForViewType { data, _ -> data is SongListCovers }
                bindViewHolder { data, _, _ ->
                    val listCovers = (data as SongListCovers).listCovers
                    val rvCover = itemView as RecyclerView
                    rvCover.onFlingListener = null
                    val snapHelper = PagerSnapHelper()
                    snapHelper.attachToRecyclerView(rvCover)
                    rvCover.adapter = object : RecyclerView.Adapter<SongListCoverViewHolder>() {
                        override fun onCreateViewHolder(
                            parent: ViewGroup,
                            viewType: Int
                        ): SongListCoverViewHolder {
                            return SongListCoverViewHolder(
                                R.layout.item_song_list_cover.inflate(
                                    parent
                                )
                            )
                        }

                        override fun getItemCount() = listCovers.size
                        override fun onBindViewHolder(
                            holder: SongListCoverViewHolder,
                            position: Int
                        ) {
                            val cover = listCovers[position]
                            holder.coverIv.loadImg(cover.cover)
                            holder.coverName.text = cover.listName
                            holder.itemView.fastClickListener {
//                                LogUtil.e("歌单类型：${cover.type}")
                                mViewModel.findSongListByType(cover.type)
                            }
                        }
                    }
                }
            }
            addItem(R.layout.item_change_title) {
                isForViewType { data, _ -> data is SingleSongTitle }
                bindViewHolder { data, position, holder ->
                    val title = data as SingleSongTitle
                    setText(R.id.find_tv_title_three, title.title)
                    setText(R.id.find_change_second_three, title.text)
                }
            }
            addItem(R.layout.item_single_song) {
                isForViewType { data, position -> data is SingleSong }
                bindViewHolder { data, position, holder ->
                    data?.let {
                        val singleSong = it as SingleSong
                        holder.itemView?.findViewById<ImageView>(R.id.single_song_iv_cover)
                            ?.loadImg(singleSong.cover?:"")
                        setText(R.id.single_song_tv_name, singleSong.songName)
                        setText(R.id.single_song_tv_singer, singleSong.singer)
                        setText(R.id.single_song_tv_desc, singleSong.desc)
                    }
                }
            }
            addItem(R.layout.item_poetry_song_title) {
                isForViewType { data, _ -> data is PoetrySongTitle }
                bindViewHolder { data, position, holder ->
                    data?.let {
                        val title = it as PoetrySongTitle
                        setText(R.id.find_tv_poetry, title.title)
                    }
                }
            }
            addItem(R.layout.item_poetry_song) {
                isForViewType { data, position -> data is PoetrySong }
                bindViewHolder { data, position, holder ->
                    data?.let {
                        val poetrySong = it as PoetrySong
                        val leftIv =
                            itemView?.findViewById<ShapeableImageView>(R.id.find_sp_iv_icon)
                        leftIv?.shapeAppearanceModel = ShapeAppearanceModel.Builder()
                            .setAllCornerSizes(ShapeAppearanceModel.PILL).build()
                        leftIv?.loadImg(poetrySong.cover?:"")
                        setText(R.id.find_sp_tv_title, poetrySong.name)
                        setText(R.id.find_tv_sp_author, poetrySong.singer)
                        setText(R.id.poetry_song_tv_desc, poetrySong.desc)
                    }
                }
            }
        }
//            .attach(rvListenSong)
        // JRecyclerView
        jAdapter = JRefreshAndLoadMoreAdapter(requireContext(), rawAdapter).apply {
            setOnRefreshListener {
                mViewModel.getListenData()
                jAdapter?.setRefreshComplete()
            }
        }
        jAdapter?.setIsOpenLoadMore(false)
//        jAdapter?.refreshLoadView = MaiRefreshView(requireContext())
        rvListenSong.adapter = jAdapter
    }

    override fun initData() {
        super.initData()
//        if (isNewDay()) {
            mViewModel.getListenData()
//        }
    }

    override fun observe() {
        super.observe()
        mViewModel.listData.observe(this, Observer {
            it?.let {
//                rawAdapter?.submitList(it)
                rawAdapter?.items = it
                jAdapter?.notifyDataSetChanged()
            }
        })
        mViewModel.specialSongList.observe(this,Observer{
//            LogUtil.e("specialSongList$it")
            if (isNotNullOrEmpty(it)) {
            }
        })
    }

    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bannerIv: ImageView = itemView.findViewById(R.id.item_banner_iv)
        val bannerTvName: TextView = itemView.findViewById(R.id.item_banner_tv_name)
    }

    class SongListCoverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coverIv: ImageView = itemView.findViewById(R.id.item_song_list_iv_cover)
        val coverName: TextView = itemView.findViewById(R.id.item_song_list_tv_desc)
    }
}