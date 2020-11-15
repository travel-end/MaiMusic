package wind.maimusic.ui.fragment.songlist

import kotlinx.android.synthetic.main.fragment_song_list_top_detail.*
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.vm.SongListTopDetailViewModel
import wind.widget.utils.loadImg

class SongListTopDetailFragment :BaseLifeCycleFragment<SongListTopDetailViewModel>(){
    private var detailCover:String=""
    private var detailName:String=""
    private var detailLanguage:String=""
    private var detailCompany:String=""
    private var detailDesc:String=""
    private var detailPublicTime:String=""
    private var detailType:String=""
    companion object {
        const val DETAIL_COVER = "detail_cover"
        const val DETAIL_NAME = "detail_name"
        const val DETAIL_LANGUAGE = "detail_language"
        const val DETAIL_COMPANY = "detail_company"
        const val DETAIL_DESC = "detail_desc"
        const val DETAIL_PUBLIC_TIME= "detail_public_time"
        const val DETAIL_TYPE= "detail_type"
    }
    override fun layoutResId()=R.layout.fragment_song_list_top_detail

    override fun initView() {
        super.initView()
        getBundle()
        detailIvCover.loadImg(detailCover)
        tv_album_name.text = detailName
        tv_language.text = detailLanguage
        tv_company.text = detailCompany
        tv_public_time.text = detailPublicTime
        tv_album_type.text = detailType
        tv_desc.text = detailDesc
    }
    private fun getBundle() {
        val b = arguments
        b?.let {
            detailCover= it.getString(DETAIL_COVER,"")
            detailName = it.getString(DETAIL_NAME,"")
            detailLanguage = it.getString(DETAIL_LANGUAGE,"")
            detailCompany = it.getString(DETAIL_COMPANY,"")
            detailDesc = it.getString(DETAIL_DESC,"")
            detailPublicTime = it.getString(DETAIL_PUBLIC_TIME,"")
            detailType = it.getString(DETAIL_TYPE,"")
        }
    }

}