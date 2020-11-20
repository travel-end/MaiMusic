package wind.maimusic.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import wind.maimusic.R
import wind.maimusic.model.listensong.SongListCover
import wind.maimusic.utils.getStringRes
import wind.maimusic.utils.inflate
import wind.maimusic.utils.isNotNullOrEmpty
import wind.widget.jrecyclerview.config.JRecycleConfig
import wind.widget.jrecyclerview.swipe.JSwipeViewHolder
import wind.widget.utils.fastClickListener
import wind.widget.utils.loadImg
import java.lang.ref.WeakReference

/**
 * 侧滑
 */
class LateralSpreadsAdapter(context: Context) :
    RecyclerView.Adapter<LateralSpreadsAdapter.LateralSpreadsViewHolder>() {
    private var mContext: WeakReference<Context>? = null
    var mDataList: MutableList<SongListCover>? = null
    private var onOnLsItemClickListener: OnLsItemClickListener<SongListCover>? = null
    companion object {
        const val TYPE_DELETE = 0
        const val TYPE_EDIT = 1
    }

    init {
        mContext = WeakReference(context)
    }

    fun refreshData(list: MutableList<SongListCover>?) {
        this.mDataList = list
        notifyDataSetChanged()
    }

    fun addData(position: Int, item: SongListCover) {
        this.mDataList?.add(position, item)
        notifyItemChanged(position)
    }

    fun removeData(position: Int) {
        this.mDataList?.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addDataRecent(item: SongListCover) {
        this.mDataList?.add(0, item)
        notifyItemInserted(0)
    }

    class LateralSpreadsViewHolder(itemView: View) : JSwipeViewHolder(itemView) {
        var tvRightMenu: TextView? = null
        var tvRightMenuTwo: TextView? = null
        var ivCover: ImageView? = null
        var tvSongListTitle: TextView? = null
        var tvSongListNum: TextView? = null
        var tvContent: TextView? = null
        var rlContentView: ConstraintLayout? = null

        override fun getContentLayout() = R.layout.view_swipe_content

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

        override fun getRightMenuLayout() = R.layout.view_swipe_right_menu
        override fun initRightMenuItem(flRightMenu: FrameLayout?) {
            super.initRightMenuItem(flRightMenu)
            tvRightMenu = flRightMenu?.findViewById(R.id.tv_right_menu)
            tvRightMenuTwo = flRightMenu?.findViewById(R.id.tv_right_menu_two)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LateralSpreadsViewHolder {
        return LateralSpreadsViewHolder(JRecycleConfig.SWIPE_LAYOUT.inflate(parent))
    }

    override fun getItemCount() = mDataList?.size ?: 0

    override fun onBindViewHolder(holder: LateralSpreadsViewHolder, position: Int) {
        holder.run {
            if (isNotNullOrEmpty(mDataList)) {
                val item = mDataList!![position]
                tvRightMenu?.text = R.string.delete.getStringRes()
                tvRightMenu?.fastClickListener {
                    onOnLsItemClickListener?.onItemClick(TYPE_DELETE, item, position)
                    swipeItemLayout.close()
                }
                tvRightMenuTwo?.text = R.string.edit.getStringRes()
                tvRightMenuTwo?.fastClickListener {
                    onOnLsItemClickListener?.onItemClick(TYPE_EDIT, item, position)
                    swipeItemLayout.close()
                }
                ivCover?.loadImg(item.cover ?: "")
                tvSongListTitle?.text = item.listName
//                tvSongListNum?.text = if (mDataList)
                rlContentView?.fastClickListener {
                    onOnLsItemClickListener?.onHoldItemClick(it,item)
                }
            }

        }
    }

    fun setOnLsItemClickListener(listener: OnLsItemClickListener<SongListCover>) {
        this.onOnLsItemClickListener = listener
    }
}