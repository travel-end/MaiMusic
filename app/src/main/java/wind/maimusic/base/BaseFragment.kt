package wind.maimusic.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import wind.maimusic.Constants
import java.lang.ref.WeakReference

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
        return mRootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initStatusBar()
        initView()
    }

    override fun onStart() {
        super.onStart()
        initData()
        initAction()

    }
    open fun initView() {

    }
    open fun initData() {

    }
    open fun initAction() {

    }
    open fun initStatusBar() {

    }
    open fun requireLazyInit(isRequire:Boolean=false) {
        if (isRequire) {
            lazyHandler.sendEmptyMessageDelayed(Constants.MSG_CODE,200)
        }
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