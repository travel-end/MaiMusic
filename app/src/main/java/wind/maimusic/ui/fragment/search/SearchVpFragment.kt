package wind.maimusic.ui.fragment.search

import android.os.Bundle
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseVpFragment
import wind.maimusic.utils.getStringRes

class SearchVpFragment : BaseVpFragment() {
    private var searchText: String = ""

    companion object {
        fun newInstance(searchContent: String): SearchVpFragment {
            val fragment = SearchVpFragment()
            fragment.arguments = Bundle().apply {
                putString(Constants.KEY_SEARCH_CONTENT, searchContent)
            }
            return fragment
        }
    }

    override fun layoutResId() = R.layout.fragment_search_vp
    override fun initView() {
        super.initView()
        searchText = arguments?.getString(Constants.KEY_SEARCH_CONTENT) ?: ""
        initVpTitle(
            arrayOf(
                R.string.single_song.getStringRes(),
                R.string.album.getStringRes()
            )
        )
        initVpFragments(
            arrayOf(
                SearchSingleSongFragment.newInstance(searchText),
                SearchAlbumFragment.newInstance(searchText)
            )
        )
    }
}