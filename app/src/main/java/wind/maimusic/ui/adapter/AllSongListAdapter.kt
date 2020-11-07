package wind.maimusic.ui.adapter

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import wind.maimusic.R
import wind.maimusic.model.listensong.SongListCovers
import wind.maimusic.utils.LogUtil
import wind.maimusic.utils.gone
import wind.maimusic.utils.inflate

class AllSongListAdapter(private val context: Context,dataList:List<SongListCovers>?,layoutBottom: IntArray):RecyclerView.Adapter<AllSongListAdapter.AllSongListViewHolder>() {
    private var onSongListItemClickListener:OnSongListItemClickListener?=null
    private var list:List<SongListCovers>?=null
    private var mIsFirstLoad:Boolean = true
    private var mView:View?=null
    private var mRecyclerViewMaxHeight:Int = 0
    private val mHandler = Handler(Looper.getMainLooper())
    companion object {
        const val  GRID_ITEM_SPACE = 24
        const val GRID_SPACE_COUNT = 3
    }
    class AllSongListViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val tvTitle:TextView = itemView.findViewById(R.id.tv_title)
        val rvSongListCover :RecyclerView = itemView.findViewById(R.id.gv_recycler_item)
        val llItemGroup:LinearLayout = itemView.findViewById(R.id.ll_recycler_item_group)
    }
    init {
        list = dataList
        require(layoutBottom.size == 2) { "This layoutBottom must have two parameters!" }
        mRecyclerViewMaxHeight = layoutBottom[0] - layoutBottom[1]
        LogUtil.e("---AllSongListAdapter--mRecyclerViewMaxHeight:$mRecyclerViewMaxHeight")
    }
    fun refreshData(songList:List<SongListCovers>) {
        this.list = songList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllSongListViewHolder {
        return AllSongListViewHolder(R.layout.item_all_song_list.inflate(parent))
    }

    override fun getItemCount()=if (list == null) 0 else list!!.size
    fun setOnSongListItemClickListener(listener: OnSongListItemClickListener?) {
        this.onSongListItemClickListener = listener
    }
    override fun onBindViewHolder(holder: AllSongListViewHolder, position: Int) {
        list?.let {l->
            val item = l[position]
            if (item.title.isNotEmpty()) {
                holder.tvTitle.text = item.title
            } else {
                holder.tvTitle.gone()
            }
            val songList = item.listCovers
            val glm = GridLayoutManager(context,GRID_SPACE_COUNT)
            holder.rvSongListCover.layoutManager = glm
            val songListAdapter = SongListAdapter(songList)
            holder.rvSongListCover.adapter = songListAdapter
            songListAdapter.setOnSongListItemClickListener(onSongListItemClickListener)
            //获取第一个Item高度，由于所有Item的高度一致，所以获取第一个Item即可
            if (mIsFirstLoad && songList.isNotEmpty()) {
                val runnable = Runnable { //这个必须在子线程中获取，否则获取不到
                    mView = glm.findViewByPosition(0)
                    mIsFirstLoad = false
                }
                mHandler.postDelayed(runnable, 200)
            }

            /**
             * 根据支付宝效果模仿的思路
             * 思路是这样的：如果是最后一条就设置为充满全屏，否则就是自适应。
             * 解决的问题：如果都是自适应会发现根据滑动无法切换最后两条，如果数据过少有可能就不能切换。
             *
             * 修改
             * #versionCode 2
             * #versionName 1.1
             * 1.自动计算最后一条状态，如果计算高度 >= maxHeight 就设置为wrap_content, 否则设置为match_content
             */
            val layoutParams: ViewGroup.LayoutParams = holder.llItemGroup.layoutParams
            if (position == itemCount - 1) {
                if (mView != null) {
                    //获取item总数
                    val count = songList?.size ?: 0
                    //计算行数
                    val rowCount: Int =
                        if (count % GRID_SPACE_COUNT == 0) count /GRID_SPACE_COUNT else (count / GRID_SPACE_COUNT) + 1
                    //计算RecyclerView的纯高度，不包括间距
                    val itemHeight = mView!!.bottom * rowCount
                    //计算间距的总高度
                    val spaceHeight: Int =
                        (rowCount - 1) * GRID_ITEM_SPACE
                    if (itemHeight + spaceHeight >= mRecyclerViewMaxHeight) {
                        //说明以及超出屏幕范围，需设置warp_content
                        layoutParams.height = RecyclerView.LayoutParams.WRAP_CONTENT
                    } else {   //未超出，直接设置match_parent
                        layoutParams.height = RecyclerView.LayoutParams.MATCH_PARENT
                    }
                } else {       //如果为空，直接设置为match_parent
                    layoutParams.height = RecyclerView.LayoutParams.MATCH_PARENT
                }
            } else {
                layoutParams.height = RecyclerView.LayoutParams.WRAP_CONTENT
            }
            holder.llItemGroup.layoutParams = layoutParams
        }
    }
}