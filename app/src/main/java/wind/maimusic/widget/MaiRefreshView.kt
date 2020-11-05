package wind.maimusic.widget

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import wind.maimusic.R
import wind.maimusic.utils.inflate
import wind.widget.jrecyclerview.loadview.base.IBaseRefreshLoadView
import wind.widget.jrecyclerview.loadview.bean.MoveInfo

/**
 * todo 将美团的icon换成自己对应的icon
 */
class MaiRefreshView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :IBaseRefreshLoadView(context,attrs,defStyleAttr) {
    private var mIvMaiRefreshView: View?=null

    private lateinit var mLoadView:FrameLayout
    override fun onPullToAction() {
    }
    override fun onMoving(moveInfo: MoveInfo?) {
        this.mIvMaiRefreshView?.clearAnimation()
        moveInfo?.let {info->
            if (info.percent < 100) {
                val lp = this.mIvMaiRefreshView?.layoutParams
                val height = info.viewHeight * (info.percent/100)
                lp?.height = height
                lp?.width = height
                this.mIvMaiRefreshView?.setBackgroundResource(R.drawable.temp_pull)
            } else {
                this.mIvMaiRefreshView?.clearAnimation()
                this.mIvMaiRefreshView?.setBackgroundResource(R.drawable.meituan_pull_to_refresh_anim)
                val anim = this.mIvMaiRefreshView?.background as AnimationDrawable
                anim.start()
            }
        }
    }

    override fun getLoadView()=mLoadView

    override fun onDone() {
        this.mIvMaiRefreshView?.clearAnimation()
        this.mIvMaiRefreshView?.setBackgroundResource(R.drawable.temp_pull)

    }

    override fun onReleaseToAction() {
    }
    override fun onExecuting() {
        this.mIvMaiRefreshView?.clearAnimation()
        this.mIvMaiRefreshView?.setBackgroundResource(R.drawable.meituan_refreshing_anim)
        val anim = this.mIvMaiRefreshView?.background as AnimationDrawable
        anim.start()
    }

    override fun initView(context: Context?): View {
//        mLoadView = R.layout.view_mai_refresh.inflate(this) as FrameLayout
        mLoadView = LayoutInflater.from(context).inflate(R.layout.view_mai_refresh,this,false) as FrameLayout
        this.mIvMaiRefreshView = mLoadView.findViewById(R.id.iv_mai_refresh)
        this.mIvMaiRefreshView?.setBackgroundResource(R.drawable.temp_pull)
        return mLoadView
    }
}