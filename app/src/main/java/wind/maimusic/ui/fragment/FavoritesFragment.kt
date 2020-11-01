package wind.maimusic.ui.fragment

import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_collect.*
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.vm.FavoritesViewModel
import wind.widget.utils.fastClickListener

class FavoritesFragment:BaseLifeCycleFragment<FavoritesViewModel>() {
    companion object {
        fun newInstance():Fragment{
            return FavoritesFragment()
        }
    }
    override fun layoutResId()=R.layout.fragment_collect

    override fun initAction() {
        super.initAction()
        collect_tv_local_song.fastClickListener {
            Navigation.findNavController(it).navigate(R.id.to_local_song_fragment)
        }
        collect_tv_recent_read.fastClickListener {
            Navigation.findNavController(it).navigate(R.id.to_recent_read_fragment)
        }
        collect_tv_recent_listen.fastClickListener {
            Navigation.findNavController(it).navigate(R.id.to_recent_listen_fragment)
        }
        collect_tv_downloaded.fastClickListener {
            Navigation.findNavController(it).navigate(R.id.to_downloaded_fragment)
        }
        collect_fl_loved_song.fastClickListener {
            Navigation.findNavController(it).navigate(R.id.to_loved_song_fragment)
        }
        collect_fl_loved_poetry.fastClickListener {
            Navigation.findNavController(it).navigate(R.id.to_loved_poetry_fragment)
        }
    }
}