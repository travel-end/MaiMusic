package wind.maimusic.ui.fragment.search

import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.utils.isNotNullOrEmpty
import wind.maimusic.vm.SearchMainViewModel
import wind.widget.edittext.SearchEditText

/**
 * @By Journey 2020/10/27
 * @Description
 */
class SearchMainFragment :BaseLifeCycleFragment<SearchMainViewModel>(){
    private lateinit var searchEditText:SearchEditText
    override fun layoutResId()= R.layout.fragment_search_main
    override fun initView() {
        super.initView()
        searchEditText = mRootView.findViewById(R.id.view_search_et)
        arguments?.let {
            val recommendSearch = it.getString(Constants.HOT_SEARCH)
            if (recommendSearch.isNotNullOrEmpty()) {
                searchEditText.hint = recommendSearch
            }
        }

        replaceFragment(SearchHotFragment.newInstance())
    }

    override fun initAction() {
        super.initAction()
        searchEditText.doAfterTextChanged {

        }
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager
            .beginTransaction()
            .replace(
                R.id.main_search_fl_container,
                fragment
            )
            .commitAllowingStateLoss()
    }
}