package wind.maimusic.widget

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import wind.maimusic.utils.SongUtil
import wind.widget.R
import wind.widget.interf.OnPlayerViewClickListener
import wind.widget.model.Song
import wind.widget.progressbar.NumberProgressBar
import wind.widget.utils.fastClickListener
import wind.widget.utils.loadImg

/**
 * @By Journey 2020/10/25
 * @Description
 */
class BottomPlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private lateinit var ivSongCover: ShapeableImageView
    private lateinit var playProgressBar: NumberProgressBar
    private lateinit var tvSongName: TextView
    private lateinit var tvSongSinger: TextView
    private lateinit var ivPlayingBtn: ImageView
    private lateinit var ivSongList: ImageView
    private var onPlayerViewClickListener: OnPlayerViewClickListener? = null

    init {
        initView()
    }
    private fun setOnPlayerViewClickListener(listener: OnPlayerViewClickListener) {
        onPlayerViewClickListener = listener
    }

    private fun initView() {
        val playView = LayoutInflater.from(context).inflate(R.layout.bottom_play_view, this, true)
        playView?.run {
            ivSongCover = findViewById(R.id.play_view_iv_song_cover)
            ivSongCover.shapeAppearanceModel =
                ShapeAppearanceModel.Builder().setAllCornerSizes(ShapeAppearanceModel.PILL).build()
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

    fun setCurrentSongInfo(song: Song) {
        // 本地音乐
        if (song.imgUrl == null) {
            SongUtil.loadLocalSongCover(song.singer ?: "", ivSongCover)
        } else {// 网络音乐
            ivSongCover.loadImg(
                song.imgUrl ?: "",
                placeholder = R.drawable.disk,
                error = R.drawable.disk
            )
        }
        tvSongName.text = song.songName
        tvSongSinger.text = song.singer
        playProgressBar.max = song.duration
        playProgressBar.progress = song.currentTime.toInt()
    }

    fun setPlayingStatus(isSelected: Boolean) {
        ivPlayingBtn.isSelected = isSelected
    }

    fun startCoverRotation() {
        rotationAnim.start()
    }

    private val rotationAnim by lazy {
        ObjectAnimator.ofFloat(ivSongCover, "rotation", 0.0f, 360.0f).apply {
            duration = 30000
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
        }
    }

    fun clearAnim() {
        if (rotationAnim.isRunning) {
            rotationAnim.cancel()
        }
    }
}