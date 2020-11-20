package wind.maimusic.ui.fragment.mime

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.fragment_md_mine.*
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.utils.CuterManager
import wind.maimusic.utils.LogUtil
import wind.maimusic.utils.imageSelect
import wind.maimusic.utils.isNotNullOrEmpty
import wind.maimusic.vm.MimeViewModel
import wind.widget.imageselector.util.ImageSelector
import wind.widget.utils.fastClickListener
import wind.widget.utils.loadImg
import wind.widget.utils.toIntPx

/**
 * @By Journey 2020/11/18
 * @Description
 */
class MimeFragment : BaseLifeCycleFragment<MimeViewModel>() {
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var toolBar: Toolbar
    private var alphaFlag: Boolean = true
    private var immersionBar: ImmersionBar? = null
    private var imageType: Int = 0

    companion object {
        private const val IMAGE_TYPE_AVATAR = 0
        private const val IMAGE_TYPE_BG = 1
    }

    override fun layoutResId() = R.layout.fragment_md_mine
    override fun initView() {
        super.initView()
        appBarLayout = mRootView.findViewById(R.id.md_song_list_appbar_layout)
        toolBar = mRootView.findViewById(R.id.md_song_list_toolbar)
        initToolBar()
        mime_cuter_iv_avatar.fastClickListener {
            imageType = IMAGE_TYPE_AVATAR
            requireActivity().imageSelect()
        }
        md_cuter_iv_bg.fastClickListener {
            imageType = IMAGE_TYPE_BG
            requireActivity().imageSelect()
        }
        val cuter = CuterManager.getCuterInfo()
        if (cuter != null) {
            mime_cuter_iv_avatar.loadImg(cuter.cuterCover ?: "")
            md_cuter_tv_name.text = cuter.nickName
            if (cuter.cuterBg != null) {
                md_cuter_iv_bg.loadImg(cuter.cuterBg ?: "")
            } else {
                md_cuter_iv_bg.loadImg(Constants.TEMP_DEFAULT_MIME_BG)
            }
        }
    }

    override fun initData() {
        super.initData()
        ImageSelector.preload(requireContext())
    }

    override fun observe() {
        super.observe()
        // TODO: 2020/11/19 共享viewModel如何取消数据监听？ LiveData对象属于MainActivity，生命周期跟随activity而不是fragment
        shareViewModel.getSelectImage().observe(this,Observer<String> {
            LogUtil.e("-----MimeFragment---observe image:$it")
            LogUtil.e("imageType:$imageType")
            // TODO: 2020/11/19 将默认的图片
            if (imageType == IMAGE_TYPE_AVATAR) {
                mime_cuter_iv_avatar.loadImg(it ?: "")
            } else if (imageType == IMAGE_TYPE_BG) {
                md_cuter_iv_bg.loadImg(it ?: "")
            }
            updateCuterInfo(it)
        })
    }

    private fun updateCuterInfo(img: String) {
        val cuterInfo = CuterManager.getCuterInfo()
        if (cuterInfo != null) {
            when (imageType) {
                IMAGE_TYPE_AVATAR -> {
                    cuterInfo.cuterCover = img
                }
                IMAGE_TYPE_BG -> {
                    cuterInfo.cuterBg = img
                }
            }
            CuterManager.setCuter(cuterInfo)
        }
    }

    private fun initToolBar() {
        val alphaMaxOffset = Constants.TOOLBAR_MAX_OFFSET.toFloat().toIntPx()
        toolBar.background?.alpha = 0
        md_song_list_tv_title_name?.alpha = 0f
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (verticalOffset > -alphaMaxOffset) {
                toolBar.background?.alpha = 255 * -verticalOffset / alphaMaxOffset
                var textAlpha = -verticalOffset.toFloat() / 1000
                if (textAlpha > 1) {
                    textAlpha = 1.00f
                }
                md_song_list_tv_title_name?.alpha = textAlpha
                if (alphaFlag) {
                    alphaFlag = false
                    md_song_list_iv_title_back?.setImageResource(R.drawable.ic_arrow_left_white)
                    immersionBar?.statusBarDarkFont(false)?.init()
                }
            } else {
                if (!alphaFlag) {
                    alphaFlag = true
                    toolBar.background?.alpha = 255
                    md_song_list_tv_title_name?.alpha = 1.0f
                    md_song_list_iv_title_back?.setImageResource(R.drawable.ic_arrow_left)
                    immersionBar?.statusBarDarkFont(true)?.init()
                }
            }
        })
    }
}