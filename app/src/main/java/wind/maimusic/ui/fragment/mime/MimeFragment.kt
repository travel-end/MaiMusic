package wind.maimusic.ui.fragment.mime

import kotlinx.android.synthetic.main.fragment_md_mine.*
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.vm.MimeViewModel
import wind.widget.imageselector.util.ImageSelector
import wind.widget.utils.fastClickListener

/**
 * @By Journey 2020/11/18
 * @Description
 */
class MimeFragment:BaseLifeCycleFragment<MimeViewModel>() {
    override fun layoutResId()=R.layout.fragment_md_mine

    override fun initView() {
        super.initView()
        mime_iv_avatar.fastClickListener {
            ImageSelector.builder()
                .useCamera(true)
                .setCrop(true)
                .setCropRatio(1.0f)
                .setSingle(true)
                .canPreview(true)
                .start(requireActivity(),Constants.PIC_REQUEST_CODE)
        }
    }

    override fun initData() {
        super.initData()
        ImageSelector.preload(requireContext())
    }
}