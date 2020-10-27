package wind.maimusic.ui.fragment.search

import android.text.Editable
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.utils.LogUtil
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
        searchEditText.setOnSearchEditTextListener(object :SearchEditText.OnSearchEditTextListener{
            override fun onClear() {
                replaceFragment(SearchHotFragment.newInstance())
            }
            override fun afterTextChange(s: Editable?) {
//                val content = s?.toString()
//                if (content.isNotNullOrEmpty()) {
//                    replaceFragment(SearchVpFragment.newInstance(content!!))
//                }
            }
        })
        searchEditText.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                val content = textView.text.toString()
                LogUtil.e("$content")
                if (content.isNotNullOrEmpty()) {
                    replaceFragment(SearchVpFragment.newInstance(content))
                }
            }
            false
        }
    }
//    private fun switchFragment(add:Boolean,show:Boolean) {
//        val ft = childFragmentManager.beginTransaction()
//        when {
//            add -> {
//                ft.add(R.id.main_search_fl_container,hotFragment!!)
//            }
//            show -> {
//                ft.hide(vpFragment!!)
//                ft.show(hotFragment!!)
//            }
//            else -> {
//                ft.hide(hotFragment!!)
//                ft.replace(R.id.main_search_fl_container,vpFragment!!)
//            }
//        }
//        ft.commitAllowingStateLoss()
//
//    }
    private fun replaceFragment(fragment: Fragment) {
    childFragmentManager.beginTransaction().replace(R.id.main_search_fl_container,fragment).commitAllowingStateLoss()
}
}