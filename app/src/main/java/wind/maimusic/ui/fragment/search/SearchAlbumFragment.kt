package wind.maimusic.ui.fragment.search

import androidx.fragment.app.Fragment
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.vm.SearchResultViewModel

class SearchAlbumFragment:BaseLifeCycleFragment<SearchResultViewModel>() {
    companion object {
        fun newInstance():Fragment {
            return SearchAlbumFragment()
        }
    }
    override fun layoutResId()=R.layout.fragment_search_mv
}