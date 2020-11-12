package wind.maimusic.ui.fragment.search

import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import wind.maimusic.Constants
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.utils.*
import wind.maimusic.vm.SearchMainViewModel
import wind.widget.edittext.SearchEditText
import wind.widget.play.ImUtils
import wind.widget.utils.fastClickListener

/**
 * @By Journey 2020/10/27
 * @Description
 */
class SearchMainFragment : BaseLifeCycleFragment<SearchMainViewModel>() {
    private lateinit var searchEditText: SearchEditText
    private lateinit var searchTv: TextView
    private lateinit var searchBackIv: ImageView
    private lateinit var searchVpFragment: SearchVpFragment
    private var isSearchVp: Boolean = false
    override fun layoutResId() = R.layout.fragment_search_main
    override fun initView() {
        super.initView()
        searchEditText = mRootView.findViewById(R.id.view_search_et)
        searchTv = mRootView.findViewById(R.id.view_search_tv)
        searchBackIv = mRootView.findViewById(R.id.view_search_iv_back)
        val searchRootLayout = mRootView.findViewById<LinearLayout>(R.id.main_search_search)
        if (searchRootLayout != null) {
            val lp = searchRootLayout.layoutParams as LinearLayout.LayoutParams
            lp.topMargin = ImUtils.getStatusBarHeight(requireContext())
        }
        arguments?.let {
            val recommendSearch = it.getString(Constants.HOT_SEARCH)
            if (recommendSearch.isNotNullOrEmpty()) {
                searchEditText.hint = recommendSearch
            }
        }
    }

    override fun initData() {
        super.initData()
        requireLazyInit(true)
    }

    override fun lazyInitData() {
        super.lazyInitData()
        replaceFragment(SearchHotFragment.newInstance())
    }

    override fun initAction() {
        super.initAction()
        searchEditText.showKeyBoard(requireContext())
        searchTv.fastClickListener {
            requireActivity().hideKeyboards()
            val content = searchEditText.editableText.toString()
            if (content.isNotNullOrEmpty()) {
                isSearchVp = true
                searchVpFragment = SearchVpFragment.newInstance(content)
                replaceFragment(searchVpFragment)
            }
        }
        searchBackIv.fastClickListener {
            if (isSearchVp) {
                searchEditText.text = "".getEditableStr()
//                removeFragment(searchVpFragment)
                replaceFragment(SearchHotFragment.newInstance())
                isSearchVp = false
            } else {
                requireActivity().hideKeyboards()
                it.navUp()
            }
        }
        searchEditText.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                requireActivity().hideKeyboards()
                val content = textView.text.toString()
                if (content.isNotNullOrEmpty()) {
                    isSearchVp = true
                    searchVpFragment = SearchVpFragment.newInstance(content)
                    replaceFragment(searchVpFragment)
                }
            }
            false
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().replace(R.id.main_search_fl_container, fragment)
            .commitAllowingStateLoss()
    }

    private fun removeFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
    }
}