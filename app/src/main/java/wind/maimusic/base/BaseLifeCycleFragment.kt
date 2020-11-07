package wind.maimusic.base

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import wind.maimusic.R
import wind.maimusic.base.state.State
import wind.maimusic.base.state.StateType
import wind.maimusic.utils.getClass
import wind.maimusic.utils.getStringRes
import wind.maimusic.utils.toast
import wind.maimusic.widget.dialog.FloatDialog
import wind.widget.utils.fastClickListener

/**
 * @By Journey 2020/10/25
 * @Description
 */
abstract class BaseLifeCycleFragment<VM:BaseViewModel>:BaseFragment() {
    // 加载结果的view
    private var loadingResultView: View?=null

    // 加载歌曲的view
    private var loadingSongView: View?=null

    // 普通加载的view
    private var loadingNormalDialog: FloatDialog?=null

    // 初始化viewModel
    protected lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        initViewModel()
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        super.initView()
        initStatusView()
    }

    override fun initData() {
        super.initData()
        observe()
    }

    override fun initAction() {
        super.initAction()
        loadingResultView?.fastClickListener {
            reLoad()
        }
    }

    open fun reLoad() {
        hideLoadingResultView()
        initData()
    }

    private fun initStatusView() {
        val resultView: View? = mRootView.findViewById(R.id.loading_result_view)
        val songView: View? = mRootView.findViewById(R.id.loading_song_view)
//        val normalView: View? = mRootView.findViewById(R.id.loading_normal_view)
        if (resultView != null) {
            loadingResultView = resultView
        }
        if (songView != null) {
            loadingSongView = songView
        }
        if (loadingNormalDialog == null) {
            loadingNormalDialog =
                FloatDialog(requireContext())
        }
    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(getClass(this))
    }

    open fun showSuccess() {

    }

    open fun showLoadingSong(msg: String) {
        if (loadingSongView?.visibility == View.GONE) {
            if (msg.isEmpty()) {
                loadingSongView?.findViewById<TextView>(R.id.view_loading_song_tv_msg)?.text = R.string.loading.getStringRes()
            } else {
                loadingSongView?.findViewById<TextView>(R.id.view_loading_song_tv_msg)?.text = msg
            }
            loadingSongView?.visibility = View.VISIBLE
        }
    }

    open fun showLoadingNormal(msg: String) {
        if (loadingNormalDialog!=null) {
            if (loadingNormalDialog?.isShowing==false) {
                loadingNormalDialog!!.show()
            }
        }
    }

    open fun dismissLoadingSong() {
        if (loadingSongView?.visibility == View.VISIBLE) {
            loadingSongView?.visibility = View.GONE
        }
    }

    open fun dismissLoadingNormal() {
        if (loadingNormalDialog!=null) {
            if (loadingNormalDialog!!.isShowing) {
                loadingNormalDialog!!.dismiss()
            }
        }
    }

    // todo 自定义toast
    private fun showToast(msg: String) {
        if (msg.isNotEmpty()) {
            msg.toast()
        }
    }

    open fun showLoadingResultView(msg: String, res: Int) {
        if (loadingResultView?.visibility == View.GONE) {
            loadingResultView?.visibility = View.VISIBLE
        }
        if (res != 0) {
            loadingResultView?.findViewById<ImageView>(R.id.view_empty_iv_icon)?.setImageResource(res)
        }
        if (msg.isNotEmpty()) {
            loadingResultView?.findViewById<TextView>(R.id.view_empty_tv_msg)?.text = msg
        }
    }

    open fun hideLoadingResultView() {
        if (loadingResultView?.visibility == View.VISIBLE) {
            loadingResultView?.visibility = View.GONE
        }
    }

    open fun observe() {
        statusObserve()
    }

    private fun statusObserve() {
        mViewModel.loadStatus.observe(this, loadStatusObserver)
    }

    private val loadStatusObserver by lazy {
        Observer<State> {
            it?.let {
                when (it.code) {
                    StateType.SUCCESS -> showSuccess()
                    StateType.ERROR -> showLoadingResultView(it.msg, it.res)
                    StateType.LOADING_NORMAL -> showLoadingNormal(it.msg)
                    StateType.LOADING_SONG -> showLoadingSong(it.msg)
                    StateType.DISMISSING_NORMAL -> dismissLoadingNormal()
                    StateType.DISMISSING_SONG -> dismissLoadingSong()
                    StateType.EMPTY -> showLoadingResultView(it.msg, it.res)
                    StateType.SHOW_TOAST-> showToast(it.msg)
                }
            }
        }
    }
}