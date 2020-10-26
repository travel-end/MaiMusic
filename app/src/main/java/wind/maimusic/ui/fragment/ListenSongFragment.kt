package wind.maimusic.ui.fragment

import androidx.fragment.app.Fragment
import wind.maimusic.R
import wind.maimusic.base.BaseLifeCycleFragment
import wind.maimusic.vm.ListenSongViewModel

class ListenSongFragment:BaseLifeCycleFragment<ListenSongViewModel> (){
    override fun layoutResId()=R.layout.fragment_listen_song
    companion object {
        fun newInstance() : Fragment {
            return ListenSongFragment()
        }
    }
}