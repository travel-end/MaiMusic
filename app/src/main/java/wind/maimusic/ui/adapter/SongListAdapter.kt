package wind.maimusic.ui.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import wind.maimusic.R
import wind.maimusic.model.listensong.SongListCover
import wind.maimusic.utils.inflate
import wind.widget.utils.fastClickListener
import wind.widget.utils.loadImg

class SongListAdapter(private val dataList:List<SongListCover>):RecyclerView.Adapter<SongListAdapter.SongListViewHolder>() {
    private var onSongListItemClickListener:OnSongListItemClickListener?=null
    class SongListViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val ivCover:ImageView = itemView.findViewById(R.id.item_song_list_iv_cover)
        val tvSongListName:TextView = itemView.findViewById(R.id.item_song_list_tv_desc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListViewHolder {
        return SongListViewHolder(R.layout.item_song_list_cover_b.inflate(parent))
    }

    override fun getItemCount()=dataList.size

    override fun onBindViewHolder(holder: SongListViewHolder, position: Int) {
        val item = dataList[position]
        holder.ivCover.loadImg(item.cover)
        holder.tvSongListName.text = item.listName
        holder.itemView.fastClickListener {
            onSongListItemClickListener?.onSongListItemClick(item)
        }
    }
    fun setOnSongListItemClickListener(listener: OnSongListItemClickListener?) {
        this.onSongListItemClickListener = listener
    }
}