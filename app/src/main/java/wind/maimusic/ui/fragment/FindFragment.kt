package wind.maimusic.ui.fragment

import androidx.fragment.app.Fragment
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.utils.LogUtil
import wind.maimusic.vm.FindPoetryViewModel

class FindFragment :BaseLifeCycleFragment<FindPoetryViewModel>(){
    override fun layoutResId()=R.layout.fragment_find
    companion object {
        fun newInstance():Fragment {
            return FindFragment()
        }
    }

    override fun initView() {
        super.initView()
    }

}