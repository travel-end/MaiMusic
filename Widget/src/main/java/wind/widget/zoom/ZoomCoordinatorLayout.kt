//package wind.maimusic.widget
//
//import android.animation.ObjectAnimator
//import android.content.Context
//import android.graphics.Rect
//import android.util.AttributeSet
//import android.view.MotionEvent
//import android.view.View
//import android.view.ViewGroup
//import android.view.animation.TranslateAnimation
//import androidx.coordinatorlayout.widget.CoordinatorLayout
//import wind.maimusic.utils.LogUtil
//import kotlin.math.abs
//
///**
// * @By Journey 2020/11/12
// * @Description
// */
//class ZoomCoordinatorLayout@JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//):CoordinatorLayout(context,attrs,defStyleAttr) {
//    private var innerView:View?=null
//    private lateinit var dropZoomView:View// 需要收缩的view
//    private var mY:Float = 0f
//    private var mFirstPosition:Float = 0f
//    private var mScaling:Boolean = false
//    private var dropZoomViewWidth:Int = 0
//    private var dropZoomViewHeight:Int = 0
//    private var lastX:Float = 0f
//    private var lastY:Float = 0f
//    private var currentX:Float = 0f
//    private var currentY:Float = 0f
//    private var distanceX:Float = 0f
//    private var distanceY:Float = 0f
//    private var upDownSlide:Boolean = false
//    private var ratio:Float = 0.5f
//    private val normal = Rect()
//    private var isCount:Boolean = false
//    private fun init() {
//        if (getChildAt(0) != null) {
//            innerView = getChildAt(0)//AppBarLayout
//            LogUtil.e("---ZoomCoordinatorLayout----innerView:$innerView")
//            // 头部收缩
//            val group = getChildAt(0) as ViewGroup
//            if (group.getChildAt(0) != null) {
//                dropZoomView = group.getChildAt(0)
//            }
//        }
//    }
//
//    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        currentX = ev.x
//        currentY = ev.y
//        if (ev.action == MotionEvent.ACTION_MOVE) {
//            distanceX = currentX - lastX
//            distanceY = currentY - lastY
//            if (abs(distanceX) < abs(distanceY) && abs(distanceY) > 12) {
//                upDownSlide = true
//            }
//        }
//        lastX = currentX
//        lastY = currentY
//        if (upDownSlide && innerView!=null) {
//            commonOnTouchEvent(ev)
//        }
//        return super.dispatchTouchEvent(ev)
//    }
//
//    private fun commonOnTouchEvent(ev: MotionEvent) {
//        // 头部缩放计算
//        if (dropZoomViewWidth <= 0 || dropZoomViewHeight <= 0) {
//            dropZoomViewWidth = dropZoomView.measuredWidth
//            dropZoomViewHeight = dropZoomView.measuredHeight
//        }
//        when(ev.action) {
//            MotionEvent.ACTION_UP->{
//                mScaling = false
//                replyView()
//                // 手指松开尾部恢复
//                if (needAnimation()) {
//                    animation()
//                    isCount = false
//                }
//                clearAll()
//            }
//            MotionEvent.ACTION_MOVE->{
//                val preY = mY
//                val nowY = ev.y
//                var deltaY = (preY-nowY).toInt()
//                if (!isCount) {
//                    deltaY = 0
//                }
//                mY = nowY
//                if (needMove()) {
//                    if (normal.isEmpty) {
//                        normal.set(innerView!!.left,innerView!!.top,innerView!!.right,innerView!!.bottom)
//                    }
//                    innerView!!.layout(innerView!!.left,innerView!!.top - deltaY / 3,
//                    innerView!!.right,innerView!!.bottom - deltaY /3)
//                }
//                isCount = true
//                if (!mScaling) {
//                    if (scrollY == 0) {
//                        mFirstPosition = ev.y
//                    } else {
//                        return
//                    }
//                }
//                val distance = (ev.y - mFirstPosition*ratio).toInt()
//                if (distance < 0) {
//                    return
//                }
//                mScaling =true
//                setZoom((1+distance).toFloat())
//            }
//        }
//    }
//
//    // 回弹动画
//    private fun replyView() {
//        val distance = dropZoomView.measuredWidth - dropZoomViewWidth
//        val anim = ObjectAnimator.ofFloat(0.0f,1.0f).apply {
//            duration = (distance * 0.7).toLong()
//            addUpdateListener {
//                val cVal = (it.animatedValue) as Float
//                setZoom(distance - ((distance) * cVal))
//            }
//        }
//        anim.start()
//    }
//
//    private fun needAnimation():Boolean {
//        return !normal.isEmpty
//    }
//
//    private fun animation() {
//        // 位移动画
//        val ta = TranslateAnimation(0f,0f,innerView?.top?.toFloat()?:0f,normal.top.toFloat())
//        ta.duration = 200
//        innerView?.startAnimation(ta)
//        innerView?.layout(normal.left,normal.top,normal.right,normal.bottom)
//        normal.setEmpty()
//    }
//
//    private fun needMove():Boolean {
//        return if (innerView!= null) {
//            val offset = innerView!!.measuredHeight - height
//            val scrollY = scrollY
//            scrollY == 0 || scrollY == offset
//        } else {
//            false
//        }
//    }
//
//    private fun setZoom(s:Float) {
//        if (dropZoomViewHeight <= 0 || dropZoomViewWidth <=0) {
//            return
//        }
//        val lp = dropZoomView.layoutParams
//        if (lp != null) {
//            lp.width = (dropZoomViewWidth + s).toInt()
//            lp.height = (dropZoomViewHeight*((dropZoomViewWidth + s) / dropZoomViewWidth)).toInt()
//            (lp as MarginLayoutParams).setMargins(
//                -(lp.width - dropZoomViewWidth)/2,0,0,0
//            )
//            dropZoomView.layoutParams = lp
//        }
//    }
//
//    private fun clearAll() {
//        lastX = 0f
//        lastY = 0f
//        distanceX = 0f
//        distanceY = 0f
//        upDownSlide = false
//    }
//
//    override fun onFinishInflate() {
//        init()
//        super.onFinishInflate()
//    }
//}