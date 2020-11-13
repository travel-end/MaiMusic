package wind.maimusic.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import wind.maimusic.R
import wind.maimusic.model.core.AlListBean
import wind.maimusic.utils.setDiffColor
import wind.widget.utils.fastClickListener
import wind.widget.utils.loadImg

/**
 * @By Journey 2020/11/13
 * @Description
 */
class AlbumSongsAdapter(private val context: Context, private val searchText:String,private val dataList: List<AlListBean>) :
    RecyclerView.Adapter<AlbumSongsAdapter.AlbumSongsViewHolder>() {
    private var onAlbumItemClickListener: OnAlbumItemClickListener? = null
    fun setOnAlbumItemClickListener(listener: OnAlbumItemClickListener) {
        this.onAlbumItemClickListener = listener
    }

    class AlbumSongsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvAlbumName: TextView = itemView.findViewById(R.id.item_album_tv_name)
        var tvAlbumSinger: TextView = itemView.findViewById(R.id.item_album_tv_singer)
        var tvAlbumPublicTime: TextView = itemView.findViewById(R.id.item_album_tv_public_time)
        var ivAlbumCover:ImageView = itemView.findViewById(R.id.item_album_iv_cover)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumSongsViewHolder {
        return AlbumSongsViewHolder(
            (LayoutInflater.from(context).inflate(R.layout.item_search_album, parent, false))
        )
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: AlbumSongsViewHolder, position: Int) {
        if (dataList.isNotEmpty()) {
            val bean = dataList[position]
            holder.run {
                bean.albumPic?.let {
                    ivAlbumCover.loadImg(it,round = 8f)
                }
                tvAlbumName.text = bean.albumName
                tvAlbumSinger.text = bean.singerName
                tvAlbumPublicTime.text = bean.publicTime
                tvAlbumName.setDiffColor(searchText,bean.albumName)
                tvAlbumSinger.setDiffColor(searchText,bean.singerName)
                tvAlbumPublicTime.setDiffColor(searchText,bean.publicTime)
                itemView.fastClickListener {
                    onAlbumItemClickListener?.onAlbumItemClick(bean,position)
                }
            }
        }
    }
}