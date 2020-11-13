package wind.maimusic.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import wind.maimusic.R
import wind.maimusic.model.core.ListBean
import wind.maimusic.utils.DownloadedUtil
import wind.maimusic.utils.StringUtil
import wind.maimusic.utils.isNotNullOrEmpty
import wind.maimusic.utils.setDiffColor
import wind.widget.rippleview.RippleView
import wind.widget.utils.fastClickListener

/**
 * @By Journey 2020/11/13
 * @Description
 */
class SearchSingleSongAdapter(private val context: Context, private val searchText:String,private val dataList: List<ListBean>) :
    RecyclerView.Adapter<SearchSingleSongAdapter.SearchSingleSongViewHolder>() {
    private var onSingleSongItemClickListener: OnSingleSongItemClickListener? = null
    fun setOnSingleSongItemClickListener(listener: OnSingleSongItemClickListener) {
        this.onSingleSongItemClickListener = listener
    }

    class SearchSingleSongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvSongName: TextView = itemView.findViewById(R.id.item_search_song_list_tv_song_name)
        var tvSinger: TextView = itemView.findViewById(R.id.item_search_song_list_tv_song_singer)
        var tvAlbum: TextView = itemView.findViewById(R.id.item_search_song_list_tv_song_album)
        var tvLyric: TextView = itemView.findViewById(R.id.item_search_song_list_tv_song_lyric)
        var ivIsDownload:ImageView = itemView.findViewById(R.id.item_search_song_list_iv_downloaded)
        var ivMoreFunction:ImageView = itemView.findViewById(R.id.item_search_song_list_iv_more)
        var rippleView:RippleView = itemView.findViewById(R.id.ripple_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSingleSongViewHolder {
        return SearchSingleSongViewHolder(
            (LayoutInflater.from(context).inflate(R.layout.item_search_song, parent, false))
        )
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: SearchSingleSongViewHolder, position: Int) {
        if (dataList.isNotEmpty()) {
            val bean = dataList[position]
            holder.run {
                tvSongName.text = bean.songname
                tvSinger.text = StringUtil.getSinger(bean)
                tvAlbum.text = bean.albumname
                tvSongName.setDiffColor(searchText,bean.songname)
                tvSinger.setDiffColor(searchText, StringUtil.getSinger(bean))
                tvAlbum.setDiffColor(searchText, bean.albumname)
                ivIsDownload.isVisible = DownloadedUtil.hasDownloadedSong(bean.songmid?:"")
                if (bean.lyric.isNotNullOrEmpty()) {
                    tvLyric.text =  bean.lyric
                    tvLyric.isVisible = true
                }
                rippleView.setOnRippleCompleteListener {
                    onSingleSongItemClickListener?.onSingleSongItemClick(bean,position)
                }
                ivMoreFunction.fastClickListener {
                    onSingleSongItemClickListener?.onRightFunctionClick(it)
                }
            }
        }
    }
}