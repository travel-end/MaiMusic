package wind.maimusic.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar
import wind.maimusic.R

/**
 * @By Journey 2020/10/25
 * @Description
 */
abstract class BaseActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId())
        initView()
        initStatusBar()
        initData()
        initAction()
    }
    /**
     * 布局id
     */
    abstract fun layoutResId():Int

    open fun initView() {

    }
    open fun initData() {

    }
    open fun initAction() {

    }
    open fun initStatusBar() {
        ImmersionBar
            .with(this)
            .statusBarView(R.id.top_view)
            .statusBarColor(R.color.white)
            .transparentStatusBar()
            .statusBarDarkFont(true)
            .init()
    }
}