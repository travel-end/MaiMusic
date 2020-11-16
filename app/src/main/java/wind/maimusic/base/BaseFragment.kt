package wind.maimusic.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.utils.LogUtil
import wind.widget.play.ImUtils

/**
 * @By Journey 2020/10/25
 * @Description
 */
abstract class BaseFragment:Fragment() {
    protected lateinit var mRootView:View
    abstract fun layoutResId():Int
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(layoutResId(),container,false)
//        LogUtil.e("BaseFragment---onCreateView")
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        LogUtil.e("BaseFragment---onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        LogUtil.e("BaseFragment---onActivityCreated")
        initStatusBar()
        initView()

        initData()
        initAction()
    }

    override fun onStart() {
        super.onStart()
//        LogUtil.e("BaseFragment---onStart")
//        initData()
//        initAction()

    }
    open fun initView() {
        val titleRootLayout = mRootView.findViewById<LinearLayout>(R.id.main_title_layout)
        val normalRootLayout = mRootView.findViewById<ConstraintLayout>(R.id.normal_title_layout)
        if (titleRootLayout!= null) {
            val lp = titleRootLayout.layoutParams as LinearLayout.LayoutParams
            lp.topMargin = ImUtils.getStatusBarHeight(requireContext()) + 10// 补点偏差
        }
        if (normalRootLayout!= null) {
            val lp = normalRootLayout.layoutParams as LinearLayout.LayoutParams
            lp.topMargin = ImUtils.getStatusBarHeight(requireContext()) + 10
        }
    }
    open fun initData() {

    }
    open fun initAction() {

    }
    open fun initStatusBar() {

    }
    /*navigation带动画的切换fragment会导致在切换的时候卡顿，使用这种方法暂时屏蔽这种卡顿。需要寻找其他根本的解决办法*/
    open fun requireLazyInit(millis:Long = 200) {
        lazyHandler.sendEmptyMessageDelayed(Constants.MSG_CODE,millis)
    }

    private val lazyHandler = object :Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == Constants.MSG_CODE) {
                lazyInitData()
            }
        }
    }
    open fun lazyInitData() {

    }

    override fun onDestroy() {
        super.onDestroy()
        lazyHandler.removeCallbacksAndMessages(null)
    }
}