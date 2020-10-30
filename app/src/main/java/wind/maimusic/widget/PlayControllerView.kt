package wind.maimusic.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import wind.maimusic.R
import wind.maimusic.utils.StringUtil
import wind.widget.model.Song
import wind.widget.utils.fastClickListener

/**
 * @By Journey 2020/10/29
 * @Description
 */
class PlayControllerView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private lateinit var playCurrentProgress:TextView
    private lateinit var playProgressBar:SeekBar
    private lateinit var playTotalProgress:TextView
    private lateinit var playMode:ImageView
    private lateinit var previousSong:ImageView
    private lateinit var playButton:ImageView
    private lateinit var nextSong:ImageView
    private var onControllerViewClickListener:OnPlayControllerViewClick?=null
    init {
        initView()
    }
    private fun initView() {
        val controllerView = LayoutInflater.from(context).inflate(R.layout.play_bottom_controller,this,true)
        controllerView?.run {
            playCurrentProgress=findViewById(R.id.tv_current_time)
            playProgressBar=findViewById(R.id.sb_progress)
            playTotalProgress=findViewById(R.id.tv_total_time)
            playMode=findViewById(R.id.iv_mode)
            previousSong=findViewById(R.id.iv_prev)
            playButton=findViewById(R.id.iv_play)
            nextSong=findViewById(R.id.iv_next)
            playProgressBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                    onControllerViewClickListener?.onSeekBarStartDrag(p0)
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                    onControllerViewClickListener?.onSeekBarStopDrag(p0)
                }

            })
            playMode.fastClickListener {
                onControllerViewClickListener?.onClickPlayMode(it)
            }
            previousSong.fastClickListener {
                onControllerViewClickListener?.onClickPreviousSong(it)
            }
            playButton.fastClickListener {
                onControllerViewClickListener?.onClickPlayButton(it)
            }
            nextSong.fastClickListener {
                onControllerViewClickListener?.onClickNextSong(it)
            }
        }
    }

    fun updatePlayProgress(progress:Int) {
        playProgressBar.progress = progress
        playCurrentProgress.text = StringUtil.formatProgress(playProgressBar.progress.toLong())
    }

    fun setPlayBtnSelected(selected:Boolean) {
        playButton.isSelected = selected
    }

    fun setCurrentSong(song: Song) {
        playCurrentProgress.text = StringUtil.formatProgress(song.currentTime)
        playTotalProgress.text = StringUtil.formatProgress(song.duration.toLong())
        playProgressBar.max = song.duration
        val time = if (song.currentTime < 1L) 0 else song.currentTime.toInt()
        playProgressBar.progress = time
    }

    fun setOnPlayControllerViewClick(listener:OnPlayControllerViewClick) {
        this.onControllerViewClickListener = listener
    }
    val progressSeekBar get() = playProgressBar

    val tvCurrentProgress get() = playCurrentProgress

    interface OnPlayControllerViewClick {
        fun onClickPlayMode(v:View)
        fun onClickPreviousSong(v:View)
        fun onClickPlayButton(v: View)
        fun onClickNextSong(v: View)
        fun onSeekBarStartDrag(seekBar: SeekBar?)
        fun onSeekBarStopDrag(seekBar: SeekBar?)
    }
}