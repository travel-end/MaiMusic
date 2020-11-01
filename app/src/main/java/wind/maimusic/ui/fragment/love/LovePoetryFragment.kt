package wind.maimusic.ui.fragment.love

import wind.maimusic.R
import wind.maimusic.base.BaseSongListFragment
import wind.maimusic.vm.LovePoetryViewModel
import wind.widget.cost.Consts

class LovePoetryFragment:BaseSongListFragment<LovePoetryViewModel>() {
    override fun layoutResId()=R.layout.fragment_loved_poetry
    override fun songListType()=Consts.POETRY_TYPE_LOVED
}