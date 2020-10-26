package wind.maimusic.ui.fragment

import androidx.fragment.app.Fragment
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.vm.FavoritesViewModel

class FavoritesFragment:BaseLifeCycleFragment<FavoritesViewModel>() {
    companion object {
        fun newInstance():Fragment{
            return FavoritesFragment()
        }
    }
    override fun layoutResId()=R.layout.fragment_favorites
}