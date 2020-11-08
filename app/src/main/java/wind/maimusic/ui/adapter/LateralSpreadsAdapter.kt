package wind.maimusic.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import wind.maimusic.R
import wind.maimusic.model.songlist.SongListItem
import wind.maimusic.utils.getStringRes
import wind.maimusic.utils.gone
import wind.maimusic.utils.inflate
import wind.maimusic.utils.isNotNullOrEmpty
import wind.widget.jrecyclerview.config.JRecycleConfig
import wind.widget.jrecyclerview.swipe.JSwipeItemLayout
import wind.widget.jrecyclerview.swipe.JSwipeViewHolder
import wind.widget.utils.fastClickListener
import wind.widget.utils.loadImg
import java.lang.ref.WeakReference

/**
 * 侧滑
 */
class LateralSpreadsAdapter(context: Context):RecyclerView.Adapter<LateralSpreadsAdapter.LateralSpreadsViewHolder>() {
    private var mContext:WeakReference<Context>?=null
    var mDataList:MutableList<SongListItem>?=null
    private var isConfirmDelete:Boolean = false
    private var onOnLsItemClickListener:OnLsItemClickListener<SongListItem>?=null
//    private var mSwipeListener:JSwipeItemLayout.SwipeListener?=null
//    private var mSwipeItemLayout:JSwipeItemLayout?=null
    companion object {
        const val TYPE_DELETE = 0
        const val TYPE_EDIT = 1
    }
    init {
        mContext = WeakReference(context)
//        mDataList = dataList
    }
    fun refreshData(list:MutableList<SongListItem>?) {
        this.mDataList = list
        notifyDataSetChanged()
    }
    fun addData(position: Int,item:SongListItem) {
        this.mDataList?.add(position,item)
        notifyItemChanged(position)
    }
    fun removeData(position: Int) {
        this.mDataList?.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addDataRecent(item:SongListItem) {
        this.mDataList?.add(0,item)
        notifyItemInserted(0)
    }

    class LateralSpreadsViewHolder(itemView:View):JSwipeViewHolder(itemView) {
        var tvRightMenu:TextView?=null
        var tvRightMenuTwo:TextView?=null
        var ivCover:ImageView?=null
        var tvSongListTitle:TextView?=null
        var tvSongListNum:TextView?=null
        var tvContent:TextView?=null
        var rlContentView:RelativeLayout?=null

        override fun getContentLayout()=R.layout.view_swipe_content

        override fun initContentItem(flContent: FrameLayout?) {
            super.initContentItem(flContent)
            ivCover = flContent?.findViewById(R.id.song_list_iv_cover)
            tvSongListTitle = flContent?.findViewById(R.id.song_list_tv_title)
            tvSongListNum = flContent?.findViewById(R.id.song_list_tv_num)
            rlContentView = flContent?.findViewById(R.id.root_rl_view)
//            tvContent = flContent?.findViewById(R.id.tv_content)
        }
        override fun initItem(frameLayout: FrameLayout?) {
        }

        override fun getRightMenuLayout()=R.layout.view_swipe_right_menu
        override fun initRightMenuItem(flRightMenu: FrameLayout?) {
            super.initRightMenuItem(flRightMenu)
            tvRightMenu = flRightMenu?.findViewById(R.id.tv_right_menu)
            tvRightMenuTwo = flRightMenu?.findViewById(R.id.tv_right_menu_two)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LateralSpreadsViewHolder {
        return LateralSpreadsViewHolder(JRecycleConfig.SWIPE_LAYOUT.inflate(parent))
    }

    override fun getItemCount()=mDataList?.size?:0

    override fun onBindViewHolder(holder: LateralSpreadsViewHolder, position: Int) {
        holder.run {
            if (isNotNullOrEmpty(mDataList)) {
//                mSwipeItemLayout = swipeItemLayout
                val item = mDataList!![position]
                tvRightMenu?.text = R.string.delete.getStringRes()
                tvRightMenu?.fastClickListener {
//                    if (!isConfirmDelete) {
//                        tvRightMenu?.text = "确认删除"
//                        isConfirmDelete= true
//                    } else {
                        onOnLsItemClickListener?.onItemClick(TYPE_DELETE,item,position)
                        swipeItemLayout.close()
//                    }
                }
//                val listener = object :JSwipeItemLayout.SwipeListener{
//                    override fun onSwipeClose(view: JSwipeItemLayout?) {
//                        isConfirmDelete = false
//                    }
//
//                    override fun onSwipeOpen(view: JSwipeItemLayout?) {
//                        tvRightMenu?.text = R.string.delete.getStringRes()
//                    }
//                }
//                mSwipeListener = listener
//                swipeItemLayout.addSwipeListener(listener)
                tvRightMenuTwo?.text = R.string.edit.getStringRes()
                tvRightMenuTwo?.fastClickListener {
                    onOnLsItemClickListener?.onItemClick(TYPE_EDIT,item,position)
                    swipeItemLayout.close()
                }
                ivCover?.loadImg(item.cover?:"")
                tvSongListTitle?.text = item.title
                tvSongListNum?.text = "${item.readNum}首"
                rlContentView?.fastClickListener {
                    onOnLsItemClickListener?.onHoldItemClick(item)
                }
            }

        }
    }
//    fun removeSwipeListener() {
//        mSwipeItemLayout?.removeSwipeListener(mSwipeListener)
//    }

    fun setOnLsItemClickListener(listener:OnLsItemClickListener<SongListItem>){
        this.onOnLsItemClickListener = listener
    }
}