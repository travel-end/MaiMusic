package wind.maimusic.ui.fragment.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.vm.SearchResultViewModel

class SearchAlbumFragment:BaseLifeCycleFragment<SearchResultViewModel>() {
    companion object {
        fun newInstance(searchText:String):SearchAlbumFragment {
            val fragment = SearchAlbumFragment()
            val bundle = Bundle().apply {
                putString(Constants.KEY_SEARCH_CONTENT,searchText)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun layoutResId()=R.layout.fragment_search_mv
}