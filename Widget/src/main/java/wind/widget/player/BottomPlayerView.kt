package wind.widget.player

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import wind.widget.R
import wind.widget.interf.OnPlayerViewClickListener
import wind.widget.progressbar.NumberProgressBar
import wind.widget.utils.fastClickListener

/**
 * @By Journey 2020/10/25
 * @Description
 */
class BottomPlayerView@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :ConstraintLayout(context,attrs,defStyleAttr) {
    private lateinit var ivSongCover:ShapeableImageView
    private lateinit var playProgressBar:NumberProgressBar
    private lateinit var tvSongName:TextView
    private lateinit var tvSongSinger:TextView
    private lateinit var ivPlayingBtn:ImageView
    private lateinit var ivSongList:ImageView
    private var onPlayerViewClickListener: OnPlayerViewClickListener?=null
    init {
        initView()
    }
    private fun setOnPlayerViewClickListener(listener: OnPlayerViewClickListener) {
        onPlayerViewClickListener = listener
    }

    private fun initView() {
        val playView = LayoutInflater.from(context).inflate(R.layout.bottom_play_view,this,true)
        playView?.run {
            ivSongCover = findViewById(R.id.play_view_iv_song_cover)
            ivSongCover.shapeAppearanceModel = ShapeAppearanceModel.Builder().setAllCornerSizes(ShapeAppearanceModel.PILL).build()
            playProgressBar = findViewById(R.id.play_view_progress_bar)
            tvSongName = findViewById(R.id.play_view_tv_song_name)
            tvSongSinger = findViewById(R.id.play_view_tv_song_singer)
            ivPlayingBtn = findViewById(R.id.play_view_iv_song_play)
            ivSongList = findViewById(R.id.play_view_iv_song_list)
            fastClickListener {
                onPlayerViewClickListener?.onPlayerViewClick(it)
            }
            ivPlayingBtn.fastClickListener {
                onPlayerViewClickListener?.onPlayerBtnClick(it)
            }
            ivSongList.fastClickListener {
                onPlayerViewClickListener?.onPlayerSongListClick(it)
            }
        }
    }



}