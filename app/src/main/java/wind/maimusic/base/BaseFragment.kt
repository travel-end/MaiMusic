package wind.maimusic.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

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
//        LogUtil.e("------BaseFragment onCreateView-------")//1
        mRootView = inflater.inflate(layoutResId(),container,false)
        return mRootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        LogUtil.e("------BaseFragment onActivityCreated-------")//3
        initStatusBar()
        initView()
    }

    override fun onStart() {
        super.onStart()
//        LogUtil.e("------BaseFragment onStart-------")//4
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
}