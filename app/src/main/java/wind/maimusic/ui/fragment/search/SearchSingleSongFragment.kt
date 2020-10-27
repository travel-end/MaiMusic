package wind.maimusic.ui.fragment.search

import androidx.fragment.app.Fragment
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.vm.SearchResultViewModel

class SearchSingleSongFragment:BaseLifeCycleFragment<SearchResultViewModel>() {
    companion object {
        fun newInstance():Fragment {
            return SearchSingleSongFragment()
        }
    }

    override fun layoutResId()=R.layout.fragment_search_single_song

}